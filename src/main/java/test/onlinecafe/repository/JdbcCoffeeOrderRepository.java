package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeOrder;

import java.util.Collection;

public class JdbcCoffeeOrderRepository implements CoffeeOrderRepository {
    @Override
    public CoffeeOrder save(CoffeeOrder coffeeOrder) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public CoffeeOrder get(int id) {
        return null;
    }

    @Override
    public Collection<CoffeeOrder> getAll() {
        return null;
    }
}
