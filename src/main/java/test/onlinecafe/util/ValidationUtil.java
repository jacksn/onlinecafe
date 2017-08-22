package test.onlinecafe.util;

import test.onlinecafe.model.BaseEntity;
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

    public static <E> E requireEntity(E entity) {
        return Objects.requireNonNull(entity, "Entity must not be null");
    }

    public static <E extends BaseEntity> void requireNullId(E entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException("Entity id must be null");
        }
    }

    public static <E extends BaseEntity> void requireId(E entity) {
        if (entity.isNew()) {
            throw new IllegalArgumentException("Entity id must not be null");
        }
    }

    //    http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
