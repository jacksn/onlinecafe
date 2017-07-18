package test.onlinecafe.util;

public interface DiscountStrategy {
    double getDiscountedItemPrice(int quantity, double price, int nthCup);
    double getDeliveryPrice(double orderTotal, double deliveryCost, double freeDeliveryThreshold);
}
