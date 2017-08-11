package test.onlinecafe.service;

import test.onlinecafe.dto.CoffeeOrderDto;
import test.onlinecafe.model.CoffeeOrder;

import java.util.List;

public interface CoffeeOrderService {
    CoffeeOrder save(CoffeeOrder order);

    CoffeeOrderDto save(CoffeeOrderDto orderDto);

    CoffeeOrder update(CoffeeOrder order);

    void delete(int id);

    CoffeeOrder get(int id);

    List<CoffeeOrder> getAll();
}
