package test.onlinecafe.util.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

import java.util.Locale;

@Component("NoDiscount")
public class NoDiscount implements Discount {
    private static final Logger log = LoggerFactory.getLogger(NoDiscount.class);

    private MessageSource messageSource;
    private ConfigurationService service;

    // delivery cost
    private double m = 2;

    public NoDiscount(MessageSource messageSource, ConfigurationService configurationService) {
        this.messageSource = messageSource;
        this.service = configurationService;
    }

    @Override
    public void init() {
        try {
            this.m = Double.parseDouble(service.get("m").getValue());
        } catch (NotFoundException | NumberFormatException e) {
            log.error("Unable to get configuration parameter",  e);
        }
    }

    @Override
    public double getDiscountedItemCost(int quantity, double price) {
        return quantity * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return m;
    }

    @Override
    public String getDescription(Locale locale) {
        return "";
    }
}
