package test.onlinecafe.util;

import test.onlinecafe.util.discount.Discount;

public final class CoffeeOrderUtil {
    private static Discount discount;

    private CoffeeOrderUtil() {
    }

    public static Discount getDiscount() {
        return discount;
    }

    public static void setDiscount(Discount discount) {
        CoffeeOrderUtil.discount = discount;
    }

    public static Double getDeliveryCost(Double orderTotalCost) {
        return discount.getDeliveryCost(orderTotalCost);
    }

    public static double getDiscountedItemCost(int quantity, double price) {
        return discount.getDiscountedItemCost(quantity, price);
    }
}
