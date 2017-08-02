package test.onlinecafe.service;

import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeTypeRepository;
import test.onlinecafe.util.exception.NotFoundException;

import java.util.List;

import static test.onlinecafe.util.CoffeeTypeUtil.filterEnabled;
import static test.onlinecafe.util.ValidationUtil.*;

public class CoffeeTypeServiceImpl implements CoffeeTypeService {
    private final CoffeeTypeRepository repository;

    public CoffeeTypeServiceImpl(CoffeeTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CoffeeType save(CoffeeType type) {
        return repository.save(checkEntityNotNull(type));
    }

    @Override
    public CoffeeType update(CoffeeType type) throws NotFoundException {
        return checkEntityPresence(type.getId(), repository.save(checkEntityNotNull(type)));
    }

    @Override
    public void delete(int id) {
        checkPresence(id, repository.delete(id));
    }

    @Override
    public CoffeeType get(int id) {
        return checkEntityPresence(id, repository.get(id));
    }

    @Override
    public List<CoffeeType> getAll() {
        return repository.getAll();
    }

    @Override
    public List<CoffeeType> getEnabled() {
        return filterEnabled(repository.getAll());
    }
}
