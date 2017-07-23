package test.onlinecafe;

import test.onlinecafe.model.CoffeeType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CoffeeTypeTestData {
    public static final CoffeeType COFFEE_TYPE1 = new CoffeeType(1, "Caffè Americano", 1.00, false);
    public static final CoffeeType COFFEE_TYPE2 = new CoffeeType(2, "Caffè Latte", 2.00, false);
    public static final CoffeeType COFFEE_TYPE3 = new CoffeeType(3, "Caffè Mocha", 3.00, false);
    public static final CoffeeType COFFEE_TYPE4 = new CoffeeType(4, "Cappuccino", 3.00, false);
    public static final CoffeeType COFFEE_TYPE5 = new CoffeeType(5, "Caramel Macchiato", 5.00, true);

    public static final List<CoffeeType> COFFEE_TYPES_ALL =
            Collections.unmodifiableList(Arrays.asList(COFFEE_TYPE1, COFFEE_TYPE2, COFFEE_TYPE3, COFFEE_TYPE4, COFFEE_TYPE5));

    public static final List<CoffeeType> COFFEE_TYPES_ENABLED =
            Collections.unmodifiableList(Arrays.asList(COFFEE_TYPE1, COFFEE_TYPE2, COFFEE_TYPE3, COFFEE_TYPE4));

    private CoffeeTypeTestData() {
    }

}
