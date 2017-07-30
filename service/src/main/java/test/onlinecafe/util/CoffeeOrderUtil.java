package test.onlinecafe.util;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.util.discount.DiscountStrategy;

public final class CoffeeOrderUtil {
    private static DiscountStrategy discountStrategy;
    private static ConfigurationItem discountConfiguration;

    private CoffeeOrderUtil() {
    }

    public static ConfigurationItem getConfiguration() {
        return discountConfiguration;
    }

    public static void setConfiguration(ConfigurationItem configuration) {
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

    public static double getDiscountedItemCost(int quantity, double price) {
        return discountStrategy.getDiscountedItemCost(quantity, price);
    }
}
