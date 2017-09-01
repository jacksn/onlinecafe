package test.onlinecafe.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import test.onlinecafe.model.CoffeeOrder;
import test.onlinecafe.repository.CoffeeOrderRepository;

import javax.transaction.Transactional;
import java.util.List;

@Profile("repo-datajpa")
@Repository
public class DataJpaCoffeeOrderRepositoryImpl implements CoffeeOrderRepository {
    private JpaCoffeeOrderRepository repository;

    public DataJpaCoffeeOrderRepositoryImpl(JpaCoffeeOrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public CoffeeOrder save(CoffeeOrder order) {
        if (order.isNew()) {
            return repository.save(order);
        } else {
            throwUnsupportedOperationException();
            return null;
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    @Override
    public CoffeeOrder get(int id) {
        return repository.findOne(id);
    }

    @Override
    public List<CoffeeOrder> getAll() {
        return repository.findAll();
    }
}
