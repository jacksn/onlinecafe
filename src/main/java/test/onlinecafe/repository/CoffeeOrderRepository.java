package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeOrder;

import java.util.List;

public interface CoffeeOrderRepository {
    CoffeeOrder save(CoffeeOrder order);

    boolean delete(int id);

    CoffeeOrder get(int id);

    List<CoffeeOrder> getAll();

}
