package test.onlinecafe.service;

import test.onlinecafe.model.CoffeeOrder;

import java.util.List;

public interface CoffeeOrderService {
    CoffeeOrder save(CoffeeOrder order);

    void delete(int id);

    CoffeeOrder get(int id);

    List<CoffeeOrder> getAll();
}
