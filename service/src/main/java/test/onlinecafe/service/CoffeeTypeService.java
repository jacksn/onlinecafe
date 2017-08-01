package test.onlinecafe.service;

import test.onlinecafe.model.CoffeeType;

import java.util.List;

public interface CoffeeTypeService {
    CoffeeType save(CoffeeType type);

    void delete(int id);

    CoffeeType get(int id);

    List<CoffeeType> getAll();
}
