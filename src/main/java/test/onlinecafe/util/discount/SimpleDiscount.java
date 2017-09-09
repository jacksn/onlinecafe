package test.onlinecafe.util.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Profile("discount-simple")
@Component
public class SimpleDiscount implements Discount {
    private static final Logger log = LoggerFactory.getLogger(SimpleDiscount.class);

    private static final String DESCRIPTION_TEMPLATE_NAME = "discount.SimpleDiscount.description";
    private static final String ERROR_GETTING_PARAMETER = "Unable to get configuration parameter {}";

    private static final BigDecimal DEFAULT_FREE_DELIVERY_THRESHOLD = new BigDecimal("10.00");
    private static final BigDecimal DEFAULT_DELIVERY_COST = new BigDecimal("2.00");
    private static final int DEFAULT_QUANTITY_THRESHOLD = 5;

    private MessageSource messageSource;
    private ConfigurationService service;

    // n'th cup of one type if free
    private int n;
    // if order total > x then delivery is free
    private BigDecimal x;
    // delivery cost
    private BigDecimal m;

    public SimpleDiscount(MessageSource messageSource, ConfigurationService configurationService) {
        this.messageSource = messageSource;
        this.service = configurationService;
    }

    public void init() {
        BigDecimal bigDecimalValue = null;

        String paramValue = getParameterFromDatabase("n");
        if (paramValue != null) {
            try {
                this.n = Integer.parseInt(paramValue);
            } catch (NumberFormatException e) {
                log.error("", paramValue);
                this.n = DEFAULT_QUANTITY_THRESHOLD;
            }
        }

        paramValue = getParameterFromDatabase("x");
        if (paramValue != null) {
            try {
                bigDecimalValue = new BigDecimal(paramValue);
            } catch (NumberFormatException e) {
                bigDecimalValue = null;
            }
        }
        this.x = paramValue != null ? bigDecimalValue : DEFAULT_FREE_DELIVERY_THRESHOLD;

        paramValue = getParameterFromDatabase("m");
        this.m = paramValue != null ? new BigDecimal(paramValue) : DEFAULT_DELIVERY_COST;
    }

    private String getParameterFromDatabase(String key) {
        try {
            return service.get(key).getValue();
        } catch (NotFoundException e) {
            log.error(ERROR_GETTING_PARAMETER, key);
            return null;
        }
    }

    @Override
    public BigDecimal getDiscountedItemCost(int quantity, BigDecimal price) {
        return price.multiply(new BigDecimal(quantity - quantity / n));
    }

    @Override
    public BigDecimal getDeliveryCost(BigDecimal orderTotal) {
        return orderTotal.compareTo(x) > 0 ? BigDecimal.ZERO : m;
    }

    @Override
    public String getDescription(Locale locale) {
        String currencySymbol = messageSource.getMessage("label.currency_symbol", null, locale);
        return messageSource.getMessage(DESCRIPTION_TEMPLATE_NAME, new Object[]{n, x, currencySymbol}, locale);
    }
}
