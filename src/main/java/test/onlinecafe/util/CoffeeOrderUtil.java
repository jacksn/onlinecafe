package test.onlinecafe.util;

import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.DiscountConfiguration;
import test.onlinecafe.to.CoffeeOrderItemTo;
import test.onlinecafe.util.discount.DiscountStrategy;

public final class CoffeeOrderUtil {
    private static DiscountStrategy discountStrategy;
    private static DiscountConfiguration discountConfiguration;

    private CoffeeOrderUtil() {
    }

    public static DiscountConfiguration getConfiguration() {
        return discountConfiguration;
    }

    public static void setConfiguration(DiscountConfiguration configuration) {
        CoffeeOrderUtil.discountConfiguration = configuration;
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

    public static double getDiscountedItemPrice(int quantity, double price) {
        return discountStrategy.getDiscountedItemCost(quantity, price);
    }

    public static CoffeeOrderItem getCoffeeOrderItemFromTo(CoffeeOrderItemTo orderItemTo) {
        return new CoffeeOrderItem(orderItemTo.getId(),
                orderItemTo.getCoffeeType(),
                orderItemTo.getQuantity()
        );
    }
}
