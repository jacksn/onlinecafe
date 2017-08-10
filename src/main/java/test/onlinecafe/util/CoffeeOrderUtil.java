package test.onlinecafe.util;

import test.onlinecafe.util.discount.DiscountStrategy;

public final class CoffeeOrderUtil {
    private static DiscountStrategy discountStrategy;

    private CoffeeOrderUtil() {
    }

    public static DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    public static void setDiscountStrategy(DiscountStrategy discountStrategy) {
        CoffeeOrderUtil.discountStrategy = discountStrategy;
    }

    public static Double getDeliveryCost(Double orderTotalCost) {
        return discountStrategy.getDeliveryCost(orderTotalCost);
    }

    public static double getDiscountedItemCost(int quantity, double price) {
        return discountStrategy.getDiscountedItemCost(quantity, price);
    }
}
