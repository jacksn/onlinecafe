package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeType;

import java.util.Collection;

public class JdbcCoffeeTypeRepository implements CoffeeTypeRepository {
    @Override
    public CoffeeType save(CoffeeType coffeeType) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public CoffeeType get(int id) {
        return null;
    }

    @Override
    public Collection<CoffeeType> getAll() {
        return null;
    }
}
