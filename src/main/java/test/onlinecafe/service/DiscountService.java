package test.onlinecafe.service;

import test.onlinecafe.service.discount.Discount;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public interface DiscountService {
    Discount getActiveDiscount();

    BigDecimal getDeliveryCost(BigDecimal orderTotalCost);

    BigDecimal getDiscountedItemCost(int quantity, BigDecimal price);

    Map<String, Discount> getDiscountMap();

    boolean setActiveDiscount(String discountName);

    String getActiveDiscountName();

    String getActiveDiscountDescription(Locale locale);
}
