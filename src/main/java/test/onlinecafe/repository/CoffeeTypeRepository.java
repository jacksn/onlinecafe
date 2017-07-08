package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeType;

import java.util.Collection;

public interface CoffeeTypeRepository {
    CoffeeType save(CoffeeType coffeeType);

    void delete(int id);

    CoffeeType get(int id);

    Collection<CoffeeType> getAll();
}
