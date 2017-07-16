package test.onlinecafe;

import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.model.CoffeeOrderItem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public final class CoffeeOrderTestData {
    public static final CoffeeOrder COFFEE_ORDER1 = new CoffeeOrder(
            1,
            LocalDateTime.of(2017, 7, 1, 9, 0),
            "John Smith",
            "John's address",
            Arrays.asList(
                    new CoffeeOrderItem(1, CoffeeTypeTestData.COFFEE_TYPE1, 2),
                    new CoffeeOrderItem(2, CoffeeTypeTestData.COFFEE_TYPE4, 2),
                    new CoffeeOrderItem(3, CoffeeTypeTestData.COFFEE_TYPE5, 2)),
            25.50);
    public static final CoffeeOrder COFFEE_ORDER2 = new CoffeeOrder(
            2,
            LocalDateTime.of(2017, 7, 1, 9, 1),
            "Jane Smith",
            "Jane's address",
            Arrays.asList(
                    new CoffeeOrderItem(4, CoffeeTypeTestData.COFFEE_TYPE2, 2),
                    new CoffeeOrderItem(5, CoffeeTypeTestData.COFFEE_TYPE4, 1)),
            11.00);
    public static final CoffeeOrder COFFEE_ORDER3 = new CoffeeOrder(
            3,
            LocalDateTime.of(2017, 7, 2, 18, 0),
            "John Smith",
            "John's address",
            Arrays.asList(
                    new CoffeeOrderItem(6, CoffeeTypeTestData.COFFEE_TYPE1, 1),
                    new CoffeeOrderItem(7, CoffeeTypeTestData.COFFEE_TYPE2, 1)),
            7.00);
    public static final CoffeeOrderItem COFFEE_ORDER_ITEM1 = new CoffeeOrderItem(null, CoffeeTypeTestData.COFFEE_TYPE2, 2);
    public static final CoffeeOrderItem COFFEE_ORDER_ITEM2 = new CoffeeOrderItem(null, CoffeeTypeTestData.COFFEE_TYPE4, 1);

    public static final CoffeeOrder NEW_COFFEE_ORDER = new CoffeeOrder(
            LocalDateTime.of(2017, 7, 3, 9, 0),
            "New name",
            "New delivery address",
            Arrays.asList(
                    COFFEE_ORDER_ITEM1,
                    COFFEE_ORDER_ITEM2
            ),
            10.0
    );

    public static final List<CoffeeOrder> COFFEE_ORDERS =
            Arrays.asList(COFFEE_ORDER1, COFFEE_ORDER2, COFFEE_ORDER3);

    private CoffeeOrderTestData() {
    }

}
