package test.onlinecafe.service;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

import java.util.Objects;

import static test.onlinecafe.util.ValidationUtil.requireNotNullEntity;
import static test.onlinecafe.util.ValidationUtil.checkEntityPresence;
import static test.onlinecafe.util.ValidationUtil.checkPresence;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        this.repository = repository;
    }

    private static String requireNotNullId(String id) {
        return Objects.requireNonNull(id, "Id must not be null");
    }

    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        requireNotNullEntity(configurationItem);
        return repository.save(configurationItem);
    }

    @Override
    public void delete(String id) {
        requireNotNullId(id);
        checkPresence(id, repository.delete(id));
    }

    @Override
    public ConfigurationItem get(String id) {
        return checkEntityPresence(id, repository.get(requireNotNullId(id)));
    }
}
