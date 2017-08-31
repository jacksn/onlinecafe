package test.onlinecafe.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import test.onlinecafe.model.CoffeeType;
import test.onlinecafe.repository.CoffeeTypeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Profile("repo-datajpa")
@Repository
public class DataJpaCoffeeTypeRepositoryImpl implements CoffeeTypeRepository {
    private JpaCoffeeTypeRepository repository;

    public DataJpaCoffeeTypeRepositoryImpl(JpaCoffeeTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public CoffeeType save(CoffeeType type) {
        return repository.save(type);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    @Override
    public CoffeeType get(int id) {
        return repository.findOne(id);
    }

    @Override
    public List<CoffeeType> getAll() {
        return repository.findAll();
    }

    @Override
    public List<CoffeeType> getEnabled() {
        return repository.getEnabled();
    }
}
