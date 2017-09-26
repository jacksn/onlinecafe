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
import test.onlinecafe.service.DiscountService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static test.onlinecafe.util.CoffeeOrderUtil.getCoffeeTypeDtoListWrapper;

@Controller
@RequestMapping("/")
public class CoffeeController extends BaseController{
    public static final String MODEL_ATTR_ORDER = "orderDto";
    public static final String MODEL_ATTR_COFFEE_TYPES = "coffeeTypes";
    public static final String MODEL_ATTR_DISCOUNT_DESCRIPTION = "discountDescription";
    public static final String MESSAGE_ERROR_EMPTY_ORDER = "error.empty_order";
    public static final String MESSAGE_ORDER_ACCEPTED = "label.order_accepted";
    public static final String MESSAGE_ERROR_EMPTY_ADDRESS = "error.empty_address";

    private CoffeeTypeService coffeeTypeService;
    private CoffeeOrderService coffeeOrderService;
    private DiscountService discountService;

    public CoffeeController(MessageSource messageSource, CoffeeTypeService coffeeTypeService, CoffeeOrderService coffeeOrderService, DiscountService discountService) {
        super(messageSource);
        this.coffeeTypeService = coffeeTypeService;
        this.coffeeOrderService = coffeeOrderService;
        this.discountService = discountService;
    }

    @GetMapping
    public String root(Model model) {
        model.addAttribute(MODEL_ATTR_COFFEE_TYPES, getCoffeeTypeDtoListWrapper(coffeeTypeService.getEnabled()));
        model.addAttribute(MODEL_ATTR_DISCOUNT_DESCRIPTION, discountService.getActiveDiscount().getDescription(LocaleContextHolder.getLocale()));
        return "index";
    }

    @PostMapping
    public String prepareOrder(@Valid CoffeeTypeDtoListWrapper coffeeTypes, BindingResult bindingResult,
                               HttpSession session, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            List<CoffeeOrderItemDto> orderItemDtos = new ArrayList<>();
            BigDecimal orderTotalCost = BigDecimal.ZERO;
            if (coffeeTypes.getCoffeeTypeDtos() != null) {
                for (CoffeeTypeDto typeDto : coffeeTypes.getCoffeeTypeDtos()) {
                    if (typeDto.isSelected() && typeDto.getQuantity() > 0) {
                        CoffeeType type = coffeeTypeService.get(typeDto.getTypeId());
                        int quantity = typeDto.getQuantity();
                        BigDecimal itemCost = type.getPrice().multiply(new BigDecimal(quantity));
                        BigDecimal discountedItemCost = discountService.getDiscountedItemCost(quantity, type.getPrice());
                        boolean discounted = discountedItemCost.compareTo(itemCost) < 0;
                        orderTotalCost = orderTotalCost.add(discountedItemCost);
                        orderItemDtos.add(new CoffeeOrderItemDto(type, quantity, discountedItemCost, discounted));
                    }
                }
            }
            if (!orderItemDtos.isEmpty()) {
                BigDecimal deliveryCost = discountService.getDeliveryCost(orderTotalCost);
                orderTotalCost = orderTotalCost.add(deliveryCost);
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
}
