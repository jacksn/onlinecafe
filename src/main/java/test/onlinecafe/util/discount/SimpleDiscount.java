package test.onlinecafe.util.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

@Component("simpleDiscount")
public class SimpleDiscount implements Discount {
    public static final String ERROR_GETTING_PARAMETER = "Unable to get configuration parameter {}, using default value {}";
    private static final Logger log = LoggerFactory.getLogger(SimpleDiscount.class);
    private ConfigurationService service;

    // n'th cup of one type if free
    private Integer n = 5;
    // if order total > x then delivery is free
    private Double x = 10.0;
    // delivery cost
    private Double m = 2.0;

    public SimpleDiscount(ConfigurationService configurationService) {
        this.service = configurationService;
    }

    public void init() {
        try {
            this.n = Integer.parseInt(service.get("n").getValue());
        } catch (NumberFormatException e) {
            log.error(ERROR_GETTING_PARAMETER, "n", n);
        }
        try {
            this.x = Double.parseDouble(service.get("x").getValue());
        } catch (NumberFormatException e) {
            log.error(ERROR_GETTING_PARAMETER, "x", x);
        }
        try {
            this.m = Double.parseDouble(service.get("m").getValue());
        } catch (NotFoundException | NumberFormatException e) {
            log.error(ERROR_GETTING_PARAMETER, "m", m);
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
    public String getDescription(String template, String currencySymbol) {
        return String.format(template, n, x, currencySymbol);
    }
}
