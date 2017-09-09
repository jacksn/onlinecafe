package test.onlinecafe.util.discount;

import java.math.BigDecimal;
import java.util.Locale;

public interface Discount {
    void init();

    BigDecimal getDiscountedItemCost(int quantity, BigDecimal price);

    BigDecimal getDeliveryCost(BigDecimal orderTotal);

    String getDescription(Locale locale);
}
