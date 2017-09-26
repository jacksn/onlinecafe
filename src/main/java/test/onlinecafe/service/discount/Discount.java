package test.onlinecafe.service.discount;

import java.math.BigDecimal;
import java.util.Locale;

public interface Discount {
    default void init() {}

    BigDecimal getDiscountedItemCost(int quantity, BigDecimal price);

    BigDecimal getDeliveryCost(BigDecimal orderTotal);

    String getDescription(Locale locale);

    String getDisplayName();
}
