package test.onlinecafe.util;

import test.onlinecafe.util.exception.NotFoundException;

import java.util.Objects;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> void checkPresence(T id, boolean present) {
        if (!present) {
            throw new NotFoundException("Entity with id \"" + id + "\" not found");
        }
    }

    public static <T, E> E checkEntityPresence(T id, E entity) {
        checkPresence(id, entity != null);
        return entity;
    }

    public static <E> E checkEntityNotNull(E entity) {
        return Objects.requireNonNull(entity, "Entity must not be null");
    }
}
