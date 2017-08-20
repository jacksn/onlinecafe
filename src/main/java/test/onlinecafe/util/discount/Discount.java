package test.onlinecafe.util.discount;

import java.util.Locale;

public interface Discount {
    void init();

    double getDiscountedItemCost(int quantity, double price);

    double getDeliveryCost(double orderTotal);

    String getDescription(Locale locale);
}
