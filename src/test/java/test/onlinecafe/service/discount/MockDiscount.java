package test.onlinecafe.service.discount;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;

@Component
@Profile("discount-mock")
public class MockDiscount implements Discount {
    public static final BigDecimal DELIVERY_COST = new BigDecimal(5);
    public static final String DISCOUNT_DESCRIPTION = "Mock discount";
    public static final String DISPLAY_NAME = "Mock discount";

    @Override
    public BigDecimal getDiscountedItemCost(int quantity, BigDecimal price) {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public BigDecimal getDeliveryCost(BigDecimal orderTotal) {
        return DELIVERY_COST;
    }

    @Override
    public String getDescription(Locale locale) {
        return DISCOUNT_DESCRIPTION;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}
