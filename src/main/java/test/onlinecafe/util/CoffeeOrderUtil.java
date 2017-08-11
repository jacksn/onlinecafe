package test.onlinecafe.util;

import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.util.discount.Discount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static List<CoffeeOrderItem> getOrderItemsFromDtos(List<CoffeeOrderItemDto> orderItemDtos) {
        List<CoffeeOrderItem> orderItems = new ArrayList<>();
        for (CoffeeOrderItemDto orderItemDto : orderItemDtos) {
            orderItems.add(new CoffeeOrderItem(orderItemDto.getId(),
                    orderItemDto.getCoffeeType(),
                    orderItemDto.getQuantity()
            ));
        }
        return orderItems;
    }

    public static CoffeeOrder getOrderFromDto(CoffeeOrderDto orderDto) {
        return new CoffeeOrder(
                LocalDateTime.now().withNano(0),
                orderDto.getName(),
                orderDto.getDeliveryAddress(),
                getOrderItemsFromDtos(orderDto.getOrderItems()),
                orderDto.getCost()
        );
    }
}
