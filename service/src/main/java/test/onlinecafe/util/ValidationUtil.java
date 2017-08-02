package test.onlinecafe.util;

import test.onlinecafe.util.exception.NotFoundException;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> void checkPresence(T id, boolean present) {
        if (!present) {
            throw new NotFoundException("Entity with id \"" + id + "\" not found");
        }
    }

    public static <T1, T2> void checkPresence(T1 id, T2 entity) {
        checkPresence(id, entity != null);
    }
}
