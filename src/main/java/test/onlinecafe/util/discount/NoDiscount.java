package test.onlinecafe.util.discount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import test.onlinecafe.service.ConfigurationService;
import test.onlinecafe.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Locale;

@Profile("discount-none")
@Component
public class NoDiscount implements Discount {
    private static final Logger log = LoggerFactory.getLogger(NoDiscount.class);
    private static final BigDecimal DEFAULT_DELIVERY_COST = new BigDecimal(2);

    private MessageSource messageSource;
    private ConfigurationService service;

    // delivery cost
    private BigDecimal m;

    public NoDiscount(MessageSource messageSource, ConfigurationService configurationService) {
        this.messageSource = messageSource;
        this.service = configurationService;
    }

    @Override
    public void init() {
        try {
            this.m = new BigDecimal(service.get("m").getValue());
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
}
