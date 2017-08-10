package test.onlinecafe.util.discount;

public interface Discount {
    void init();
    double getDiscountedItemCost(int quantity, double price);
    double getDeliveryCost(double orderTotal);
}
