package test.onlinecafe.util.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Profile("discount-simple")
@Component
public class SimpleDiscount implements Discount {
    private static final String DESCRIPTION_TEMPLATE_NAME = "discount.SimpleDiscount.description";
    private static final String ERROR_GETTING_PARAMETER = "Unable to get configuration parameter {}";
    private static final Logger log = LoggerFactory.getLogger(SimpleDiscount.class);

    private MessageSource messageSource;
    private ConfigurationService service;

    // n'th cup of one type if free
    private Integer n = 5;
    // if order total > x then delivery is free
    private Double x = 10.0;
    // delivery cost
    private Double m = 2.0;

    public SimpleDiscount(MessageSource messageSource, ConfigurationService configurationService) {
        this.messageSource = messageSource;
        this.service = configurationService;
    }

    public void init() {
        Number n = getParameterFromDatabase("n");
        this.n = n != null ? n.intValue() : this.n;

        n = getParameterFromDatabase("x");
        this.x = n != null ? n.doubleValue() : this.x;

        n = getParameterFromDatabase("m");
        this.m = n != null ? n.doubleValue() : this.m;
    }

    private Number getParameterFromDatabase(String key) {
        try {
            return NumberFormat.getInstance().parse(service.get(key).getValue());
        } catch (NotFoundException | ParseException e) {
            log.error(ERROR_GETTING_PARAMETER, key);
            return null;
        }
    }

    @Override
    public double getDiscountedItemCost(int quantity, double price) {
        return (quantity - quantity / n) * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return orderTotal > x ? 0 : m;
    }

    @Override
    public String getDescription(Locale locale) {
        String currencySymbol = messageSource.getMessage("label.currency_symbol", null, locale);
        return messageSource.getMessage(DESCRIPTION_TEMPLATE_NAME, new Object[]{n, x, currencySymbol}, locale);
    }
}
