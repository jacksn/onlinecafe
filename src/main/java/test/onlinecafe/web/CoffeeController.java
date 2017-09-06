package test.onlinecafe.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.onlinecafe.dto.*;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.service.CoffeeOrderService;
import test.onlinecafe.service.CoffeeTypeService;
import test.onlinecafe.util.CoffeeOrderUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static test.onlinecafe.util.CoffeeOrderUtil.getCoffeeTypeDtoListWrapper;

@Controller
@RequestMapping("/")
public class CoffeeController {
    public static final String MODEL_ATTR_ORDER = "orderDto";
    public static final String MODEL_ATTR_COFFEE_TYPES = "coffeeTypes";
    public static final String MODEL_ATTR_NOTIFICATION = "notification";
    public static final String MODEL_ATTR_DISCOUNT_DESCRIPTION = "discountDescription";
    public static final String MESSAGE_ERROR_EMPTY_ORDER = "error.empty_order";
    public static final String MESSAGE_ORDER_ACCEPTED = "label.order_accepted";
    public static final String MESSAGE_ERROR_EMPTY_ADDRESS = "error.empty_address";

    private MessageSource messageSource;
    private CoffeeTypeService coffeeTypeService;
    private CoffeeOrderService coffeeOrderService;

    public CoffeeController(MessageSource messageSource, CoffeeTypeService coffeeTypeService, CoffeeOrderService coffeeOrderService) {
        this.messageSource = messageSource;
        this.coffeeTypeService = coffeeTypeService;
        this.coffeeOrderService = coffeeOrderService;
    }

    @GetMapping
    public String root(Model model) {
        model.addAttribute(MODEL_ATTR_COFFEE_TYPES, getCoffeeTypeDtoListWrapper(coffeeTypeService.getEnabled()));
        model.addAttribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, CoffeeOrderUtil.getDiscount().getDescription(LocaleContextHolder.getLocale()));
        return "index";
    }

    @PostMapping
    public String prepareOrder(@Valid CoffeeTypeDtoListWrapper coffeeTypes, BindingResult bindingResult,
                               HttpSession session, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            List<CoffeeOrderItemDto> orderItemDtos = new ArrayList<>();
            double orderTotalCost = 0;
            if (coffeeTypes.getCoffeeTypeDtos() != null) {
                for (CoffeeTypeDto typeDto : coffeeTypes.getCoffeeTypeDtos()) {
                    if (typeDto.isSelected() && typeDto.getQuantity() > 0) {
                        CoffeeType type = coffeeTypeService.get(typeDto.getTypeId());
                        int quantity = typeDto.getQuantity();
                        double itemCost = quantity * type.getPrice();
                        double discountedItemCost = CoffeeOrderUtil.getDiscountedItemCost(quantity, type.getPrice());
                        boolean discounted = discountedItemCost < itemCost;
                        orderTotalCost += discountedItemCost;
                        orderItemDtos.add(new CoffeeOrderItemDto(type, quantity, discountedItemCost, discounted));
                    }
                }
            }
            if (!orderItemDtos.isEmpty()) {
                double deliveryCost = CoffeeOrderUtil.getDeliveryCost(orderTotalCost);
                orderTotalCost += deliveryCost;
                CoffeeOrderDto orderDto = new CoffeeOrderDto(orderItemDtos, deliveryCost, orderTotalCost);
                session.setAttribute(MODEL_ATTR_ORDER, orderDto);
                return "redirect:order";
            }
        }
        redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, MESSAGE_ERROR_EMPTY_ORDER));
        return "redirect:/";
    }

    @GetMapping("/order")
    public String orderDetails(HttpSession session, RedirectAttributes redirectAttributes) {
        CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
        if (orderDto != null && orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            return "order";
        } else {
            redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, MESSAGE_ERROR_EMPTY_ORDER));
            return "redirect:/";
        }
    }

    @PostMapping("/order")
    public String confirmOrder(@RequestParam String name, @RequestParam @NotNull @Size(min = 1) String address,
                               RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        CoffeeOrderDto orderDto = (CoffeeOrderDto) session.getAttribute(MODEL_ATTR_ORDER);
        if (orderDto == null || orderDto.getOrderItems() == null || orderDto.getOrderItems().isEmpty()) {
            session.removeAttribute(MODEL_ATTR_ORDER);
            redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, MESSAGE_ERROR_EMPTY_ORDER));
            return "redirect:/";
        }
        if (name != null && address != null && !address.isEmpty()) {
            orderDto.setName(name);
            orderDto.setDeliveryAddress(address);
            coffeeOrderService.save(orderDto);
            session.removeAttribute(MODEL_ATTR_ORDER);
            redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.SUCCESS, MESSAGE_ORDER_ACCEPTED));
            return "redirect:/";
        } else {
            model.addAttribute("name", name);
            model.addAttribute("address", address);
            model.addAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR, MESSAGE_ERROR_EMPTY_ADDRESS));
            return "order";
        }
    }

    @GetMapping("/cancel")
    public String cancelOrder(HttpSession session) {
        session.removeAttribute(MODEL_ATTR_ORDER);
        return "redirect:/";
    }

    private Notification getLocalizedNotification(NotificationType type, String messageKey) {
        return new Notification(type, messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale()));
    }
}
