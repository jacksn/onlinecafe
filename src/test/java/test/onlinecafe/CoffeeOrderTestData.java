package test.onlinecafe;

import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.onlinecafe.CoffeeTypeTestData.*;

public final class CoffeeOrderTestData {
    public static final CoffeeOrder COFFEE_ORDER1 = new CoffeeOrder(
            1,
            LocalDateTime.of(2017, 7, 1, 9, 0),
            "John Smith",
            "John's address",
            Arrays.asList(
                    new CoffeeOrderItem(1, COFFEE_TYPE1, 2),
                    new CoffeeOrderItem(2, COFFEE_TYPE4, 2),
                    new CoffeeOrderItem(3, COFFEE_TYPE5, 2)),
            9.00);
    public static final CoffeeOrder COFFEE_ORDER2 = new CoffeeOrder(
            2,
            LocalDateTime.of(2017, 7, 1, 9, 1),
            "Jane Smith",
            "Jane's address",
            Arrays.asList(
                    new CoffeeOrderItem(4, COFFEE_TYPE2, 2),
                    new CoffeeOrderItem(5, COFFEE_TYPE4, 1)),
            5.00);
    public static final CoffeeOrder COFFEE_ORDER3 = new CoffeeOrder(
            3,
            LocalDateTime.of(2017, 7, 2, 18, 0),
            "John Smith",
            "John's address",
            Arrays.asList(
                    new CoffeeOrderItem(6, COFFEE_TYPE1, 1),
                    new CoffeeOrderItem(7, COFFEE_TYPE2, 1)),
            3.00);
    public static final CoffeeOrderItem COFFEE_ORDER_ITEM1 = new CoffeeOrderItem(null, COFFEE_TYPE2, 2);
    public static final CoffeeOrderItem COFFEE_ORDER_ITEM2 = new CoffeeOrderItem(null, COFFEE_TYPE4, 1);

    public static final List<CoffeeOrder> COFFEE_ORDERS =
            Collections.unmodifiableList(Arrays.asList(COFFEE_ORDER1, COFFEE_ORDER2, COFFEE_ORDER3));

    public static CoffeeOrder getNewCoffeeOrder() {
        return new CoffeeOrder(
                LocalDateTime.of(2017, 7, 3, 9, 0),
                "New name",
                "New delivery address",
                Arrays.asList(
                        COFFEE_ORDER_ITEM1,
                        COFFEE_ORDER_ITEM2
                ),
                10.0
        );
    }

    private CoffeeOrderTestData() {
    }

}
