package test.onlinecafe.util;

import test.onlinecafe.model.CoffeeType;

import java.util.List;
import java.util.stream.Collectors;

public final class CoffeeTypeUtil {
    private CoffeeTypeUtil() {
    }

    public static List<CoffeeType> filterEnabled(List<CoffeeType> types) {
        return types.stream()
                .filter(t -> !t.getDisabled())
                .collect(Collectors.toList());
    }
}