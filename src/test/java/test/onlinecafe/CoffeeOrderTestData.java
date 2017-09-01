package test.onlinecafe;

import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.dto.CoffeeOrderItemDto;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.onlinecafe.CoffeeTypeTestData.*;

public final class CoffeeOrderTestData {
    public static final List<CoffeeOrder> COFFEE_ORDERS =
            Collections.unmodifiableList(Arrays.asList(getCoffeeOrder1(), getCoffeeOrder2(), getCoffeeOrder3()));

    public static CoffeeOrderItem getCoffeeOrderItem1() {
        return new CoffeeOrderItem(COFFEE_TYPE2, 2);
    }

    public static CoffeeOrderItem getCoffeeOrderItem2() {
        return new CoffeeOrderItem(COFFEE_TYPE4, 1);
    }

    public static CoffeeOrder getCoffeeOrder1() {
        CoffeeOrder order = new CoffeeOrder(
                1,
                LocalDateTime.of(2017, 7, 1, 9, 0),
                "John Smith",
                "John's address",
                9.00);
        order.addOrderItem(new CoffeeOrderItem(1, COFFEE_TYPE1, 2));
        order.addOrderItem(new CoffeeOrderItem(2, COFFEE_TYPE4, 2));
        order.addOrderItem(new CoffeeOrderItem(3, COFFEE_TYPE5, 2));
        return order;
    }

    public static CoffeeOrder getCoffeeOrder2() {
        CoffeeOrder order = new CoffeeOrder(
                2,
                LocalDateTime.of(2017, 7, 1, 9, 1),
                "Jane Smith",
                "Jane's address",
                5.00);
        order.addOrderItem(new CoffeeOrderItem(4, COFFEE_TYPE2, 2));
        order.addOrderItem(new CoffeeOrderItem(5, COFFEE_TYPE4, 1));
        return order;
    }

    public static CoffeeOrder getCoffeeOrder3() {
        CoffeeOrder order = new CoffeeOrder(
                3,
                LocalDateTime.of(2017, 7, 2, 18, 0),
                "John Smith",
                "John's address",
                3.00);
        order.addOrderItem(new CoffeeOrderItem(6, COFFEE_TYPE1, 1));
        order.addOrderItem(new CoffeeOrderItem(7, COFFEE_TYPE2, 1));
        return order;
    }

    public static CoffeeOrder getNewCoffeeOrder() {
        CoffeeOrder order = new CoffeeOrder(
                LocalDateTime.of(2017, 7, 3, 9, 0),
                "New name",
                "New delivery address",
                10.0
        );
        order.addOrderItem(getCoffeeOrderItem1());
        order.addOrderItem(getCoffeeOrderItem2());
        return order;
    }

    public static CoffeeOrderDto getCoffeeOrder1Dto() {
        List<CoffeeOrderItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(new CoffeeOrderItemDto(COFFEE_TYPE1, 2, COFFEE_TYPE1.getPrice() * 2, false));
        itemDtoList.add(new CoffeeOrderItemDto(COFFEE_TYPE4, 2, COFFEE_TYPE4.getPrice() * 2, false));
        itemDtoList.add(new CoffeeOrderItemDto(COFFEE_TYPE5, 2, COFFEE_TYPE5.getPrice() * 2, false));

        CoffeeOrder order = getCoffeeOrder1();
        return new CoffeeOrderDto(
                order.getName(),
                order.getDeliveryAddress(),
                itemDtoList,
                0,
                order.getCost()
        );
    }

    private CoffeeOrderTestData() {
    }
}
