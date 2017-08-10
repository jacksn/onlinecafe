package test.onlinecafe.repository;

import test.onlinecafe.model.CoffeeType;

import java.util.List;

public interface CoffeeTypeRepository {
    CoffeeType save(CoffeeType type);

    boolean delete(int id);

    CoffeeType get(int id);

    List<CoffeeType> getAll();

}
