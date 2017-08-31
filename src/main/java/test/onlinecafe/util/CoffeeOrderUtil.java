package test.onlinecafe.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.dto.CoffeeTypeDto;
import test.onlinecafe.dto.CoffeeTypeDtoListWrapper;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.util.discount.Discount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public final class CoffeeOrderUtil {
    private static Discount discount;

    private CoffeeOrderUtil() {
    }

    public static Discount getDiscount() {
        return discount;
    }

    @Autowired
    private void setDiscount(Discount discount) {
        CoffeeOrderUtil.discount = discount;
        discount.init();
    }

    public static Double getDeliveryCost(Double orderTotalCost) {
        return discount.getDeliveryCost(orderTotalCost);
    }

    public static double getDiscountedItemCost(int quantity, double price) {
        return discount.getDiscountedItemCost(quantity, price);
    }

    public static CoffeeOrderItem getOrderItemFromDto(CoffeeOrderItemDto orderItemDto) {
        return new CoffeeOrderItem(orderItemDto.getId(),
                orderItemDto.getCoffeeType(),
                orderItemDto.getQuantity()
        );
    }

    public static CoffeeOrder getOrderFromDto(CoffeeOrderDto orderDto) {
        CoffeeOrder order = new CoffeeOrder(
                LocalDateTime.now().withNano(0),
                orderDto.getName(),
                orderDto.getDeliveryAddress(),
                orderDto.getCost()
        );
        for (CoffeeOrderItemDto orderItemDto : orderDto.getOrderItems()) {
            order.addOrderItem(getOrderItemFromDto(orderItemDto));
        }
        return order;
    }

    public static CoffeeTypeDtoListWrapper getCoffeeTypeDtoListWrapper(List<CoffeeType> coffeeTypes) {
        CoffeeTypeDtoListWrapper list = new CoffeeTypeDtoListWrapper();
        List<CoffeeTypeDto> coffeeTypeDtos = new ArrayList<>();
        for (CoffeeType type : coffeeTypes) {
            coffeeTypeDtos.add(new CoffeeTypeDto(type));
        }
        list.setCoffeeTypeDtos(coffeeTypeDtos);
        return list;
    }
}
