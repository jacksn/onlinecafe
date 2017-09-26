package test.onlinecafe.web;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.onlinecafe.dto.NotificationType;
import test.onlinecafe.service.DiscountService;

@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController{
    private DiscountService discountService;

    public SettingsController(MessageSource messageSource, DiscountService discountService) {
        super(messageSource);
        this.discountService = discountService;
    }

    @GetMapping
    public String root(Model model) {
        model.addAttribute("discountMap", discountService.getDiscountMap());
        model.addAttribute("activeDiscount", discountService.getActiveDiscountName());
        return "settings";
    }

    @PostMapping
    public String saveSettings(@RequestParam String discount, RedirectAttributes redirectAttributes) {
        if (discountService.setActiveDiscount(discount)) {
            redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.SUCCESS,"label.discount_changed"));
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute(MODEL_ATTR_NOTIFICATION, getLocalizedNotification(NotificationType.ERROR,"error.error_updating_discount"));
            return "redirect:settings";
        }
    }
}
