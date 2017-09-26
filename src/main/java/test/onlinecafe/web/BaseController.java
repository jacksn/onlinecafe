package test.onlinecafe.web;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import test.onlinecafe.dto.Notification;
import test.onlinecafe.dto.NotificationType;

public class BaseController {
    public static final String MODEL_ATTR_NOTIFICATION = "notification";
    protected MessageSource messageSource;

    public BaseController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    protected Notification getLocalizedNotification(NotificationType type, String messageKey) {
        return new Notification(type, messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale()));
    }
}
