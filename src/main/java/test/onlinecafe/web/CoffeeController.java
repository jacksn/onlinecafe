package test.onlinecafe.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import test.onlinecafe.dto.*;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.service.CoffeeOrderService;
import test.onlinecafe.service.CoffeeTypeService;
import test.onlinecafe.util.CoffeeOrderUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static test.onlinecafe.util.CoffeeOrderUtil.getCoffeeTypeDtoList;

@Controller
@RequestMapping("/")
public class CoffeeController {
    public static final String MODEL_ATTR_ORDER = "orderDto";
    public static final String MODEL_ATTR_COFFEE_TYPES = "coffeeTypes";
    public static final String MODEL_ATTR_NOTIFICATION = "notification";
    public static final String MODEL_ATTR_DISCOUNT_DESCRIPTION = "discountDescription";

    private MessageSource messageSource;
    private CoffeeTypeService coffeeTypeService;
    private CoffeeOrderService coffeeOrderService;

    public CoffeeController(MessageSource messageSource, CoffeeTypeService coffeeTypeService, CoffeeOrderService coffeeOrderService) {
        this.messageSource = messageSource;
        this.coffeeTypeService = coffeeTypeService;
        this.coffeeOrderService = coffeeOrderService;
    }

    @GetMapping
    public String root(HttpSession session, Model model) {
        Notification notification = (Notification) session.getAttribute(MODEL_ATTR_NOTIFICATION);
        if (notification != null) {
            session.removeAttribute(MODEL_ATTR_NOTIFICATION);
            model.addAttribute(MODEL_ATTR_NOTIFICATION, notification);
        }
        model.addAttribute(MODEL_ATTR_COFFEE_TYPES, getCoffeeTypeDtoList(coffeeTypeService.getEnabled()));
        model.addAttribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, CoffeeOrderUtil.getDiscount().getDescription(LocaleContextHolder.getLocale()));
        return "index";
    }

    @PostMapping
    public ModelAndView prepareOrder(@ModelAttribute("coffeeTypes") CoffeeTypeDtoListWrapper coffeeTypes, HttpSession session) {
        List<CoffeeOrderItemDto> orderItemDtos = new ArrayList<>();
        double orderTotalCost = 0;
        for (CoffeeTypeDto typeDto : coffeeTypes.getCoffeeTypeDtos()) {
            if (typeDto.isSelected()) {
                CoffeeType type = coffeeTypeService.get(typeDto.getTypeId());
                int quantity = typeDto.getQuantity();
                double itemCost = quantity * type.getPrice();
                double discountedItemCost = CoffeeOrderUtil.getDiscountedItemCost(quantity, type.getPrice());
                boolean discounted = discountedItemCost < itemCost;
                orderTotalCost += discountedItemCost;
                orderItemDtos.add(new CoffeeOrderItemDto(type, quantity, discountedItemCost, discounted));
            }
        }

        ModelAndView mav = new ModelAndView();

        if (!orderItemDtos.isEmpty()) {
            double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
            orderTotalCost += deliveryCost;
            CoffeeOrderDto orderDto = new CoffeeOrderDto(orderItemDtos, deliveryCost, orderTotalCost);
            mav.setViewName("redirect:order");
            session.setAttribute(MODEL_ATTR_ORDER, orderDto);
        } else {
            mav.setViewName("redirect:/");
            session.removeAttribute(MODEL_ATTR_ORDER);
            mav.addObject(MODEL_ATTR_NOTIFICATION,
                    getLocalizedNotification(NotificationType.ERROR, "error.empty_order"));
        }
        return mav;
    }

    @GetMapping("/order")
    public String orderDetails(HttpSession session) {
        CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
        if (orderDto != null && orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            return "order";
        } else {
            session.setAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, "error.empty_order"));
            return "redirect:/";
        }
    }

    @PostMapping("/order")
    public ModelAndView confirmOrder(@RequestParam String name, @RequestParam String address, HttpSession session) {
        CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
        if (orderDto == null || orderDto.getOrderItems() == null || orderDto.getOrderItems().isEmpty()) {
            session.removeAttribute(MODEL_ATTR_ORDER);
            session.setAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, "error.empty_order"));
            return new ModelAndView("redirect:/");
        }
        if (name != null && address != null && !address.isEmpty()) {
            orderDto.setName(name);
            orderDto.setDeliveryAddress(address);
            coffeeOrderService.save(orderDto);
            removeSessionParameters(session);
            session.setAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.SUCCESS, "label.order_accepted"));
            return new ModelAndView("redirect:/");
        } else {
            ModelAndView mav = new ModelAndView("order");
            mav.addObject("name", name);
            mav.addObject("address", address);
            mav.addObject(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, "error.empty_address"));
            return mav;
        }
    }

    @GetMapping("/cancel")
    public String cancelOrder(HttpSession session) {
        removeSessionParameters(session);
        return "redirect:/";
    }

    private Notification getLocalizedNotification(NotificationType type, String messageKey) {
        return new Notification(type, messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale()));
    }

    private void removeSessionParameters(HttpSession session) {
        session.removeAttribute(MODEL_ATTR_NOTIFICATION);
        session.removeAttribute(MODEL_ATTR_ORDER);
    }
}
