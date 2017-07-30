package test.onlinecafe.util;

import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.to.CoffeeOrderItemTo;

public final class CoffeeOrderItemUtil {

    private CoffeeOrderItemUtil() {
    }

    public static CoffeeOrderItem getOrderItemFromTo(CoffeeOrderItemTo orderItemTo) {
        return new CoffeeOrderItem(orderItemTo.getId(),
                orderItemTo.getCoffeeType(),
                orderItemTo.getQuantity()
        );
    }
}
