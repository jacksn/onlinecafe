package test.onlinecafe.util;

import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.model.CoffeeOrderItem;

public final class CoffeeOrderItemUtil {

    private CoffeeOrderItemUtil() {
    }

    public static CoffeeOrderItem getOrderItemFromDto(CoffeeOrderItemDto orderItemTo) {
        return new CoffeeOrderItem(orderItemTo.getId(),
                orderItemTo.getCoffeeType(),
                orderItemTo.getQuantity()
        );
    }
}
