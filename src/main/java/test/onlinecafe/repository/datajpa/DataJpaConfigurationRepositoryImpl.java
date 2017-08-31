package test.onlinecafe.repository.datajpa;

import org.springframework.stereotype.Repository;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

import javax.transaction.Transactional;

@Repository
public class DataJpaConfigurationRepositoryImpl implements ConfigurationRepository {
    private JpaConfigurationRepository repository;

    public DataJpaConfigurationRepositoryImpl(JpaConfigurationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        return repository.save(configurationItem);
    }


    @Transactional
    @Override
    public boolean delete(String id) {
        return repository.deleteById(id) != 0;
    }

    @Override
    public ConfigurationItem get(String id) {
        return repository.findOne(id);
    }
}
