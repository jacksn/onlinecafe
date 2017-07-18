package test.onlinecafe.util;

import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.DiscountConfiguration;

import java.util.List;

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

    public static Double getDeliveryCost(Double orderTotal) {
        return discountStrategy.getDeliveryPrice(orderTotal, discountConfiguration.getDeliveryCost(), discountConfiguration.getFreeDeliveryThreshold());
    }

    public static double getDiscountedItemPrice(CoffeeOrderItem orderItem){
        return discountStrategy.getDiscountedItemPrice(
                orderItem.getQuantity(),
                orderItem.getCoffeeType().getPrice(),
                discountConfiguration.getNthCup());
    }

    public static double getOrderTotal(List<CoffeeOrderItem> orderItems) {
        double orderTotal = 0;
        for (CoffeeOrderItem orderItem : orderItems) {
            orderTotal += getDiscountedItemPrice(orderItem);
        }
        return orderTotal;
    }
}
