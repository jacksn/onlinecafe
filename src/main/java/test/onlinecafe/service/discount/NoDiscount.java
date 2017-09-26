package test.onlinecafe.service.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Locale;

@Component
public class NoDiscount implements Discount {
    private static final Logger log = LoggerFactory.getLogger(NoDiscount.class);
    private static final BigDecimal DEFAULT_DELIVERY_COST = new BigDecimal(2);
    public static final String DISPLAY_NAME = "No discount";

    private ConfigurationService service;

    // delivery cost
    private BigDecimal m;

    public NoDiscount(ConfigurationService configurationService) {
        this.service = configurationService;
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            this.m = new BigDecimal(service.get("m"));
        } catch (NotFoundException | NumberFormatException e) {
            log.error("Unable to get configuration parameter", e);
            this.m = DEFAULT_DELIVERY_COST;
        }
    }

    @Override
    public BigDecimal getDiscountedItemCost(int quantity, BigDecimal price) {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public BigDecimal getDeliveryCost(BigDecimal orderTotal) {
        return m;
    }

    @Override
    public String getDescription(Locale locale) {
        return "";
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
