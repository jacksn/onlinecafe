package test.onlinecafe.service;

import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.repository.CoffeeOrderRepository;

import java.util.List;

import static test.onlinecafe.util.ValidationUtil.*;

public class CoffeeOrderServiceImpl implements CoffeeOrderService {
    private final CoffeeOrderRepository repository;

    public CoffeeOrderServiceImpl(CoffeeOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public CoffeeOrder save(CoffeeOrder order) {
        requireEntity(order);
        requireNullId(order);
        return repository.save(order);
    }

    @Override
    public CoffeeOrder update(CoffeeOrder order) {
        requireEntity(order);
        requireId(order);
        return checkEntityPresence(order.getId(), repository.save(order));
    }

    @Override
    public void delete(int id) {
        checkPresence(id, repository.delete(id));
    }

    @Override
    public CoffeeOrder get(int id) {
        return checkEntityPresence(id, repository.get(id));
    }

    @Override
    public List<CoffeeOrder> getAll() {
        return repository.getAll();
    }
}
