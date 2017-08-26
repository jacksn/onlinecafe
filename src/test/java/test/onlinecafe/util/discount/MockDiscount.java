package test.onlinecafe.util.discount;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Profile("discount-mock")
public class MockDiscount implements Discount {
    public static double DELIVERY_COST = 5;
    public static String DISCOUNT_DESCRIPTION = "Mock discount";

    @Override
    public void init() {

    }

    @Override
    public double getDiscountedItemCost(int quantity, double price) {
        return quantity * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return DELIVERY_COST;
    }

    @Override
    public String getDescription(Locale locale) {
        return DISCOUNT_DESCRIPTION;
    }
}
