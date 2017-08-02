package test.onlinecafe.service;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

import java.util.Objects;

import static test.onlinecafe.util.ValidationUtil.checkEntityNotNull;
import static test.onlinecafe.util.ValidationUtil.checkEntityPresence;
import static test.onlinecafe.util.ValidationUtil.checkPresence;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        this.repository = repository;
    }

    private static String requireIdNotNull(String id) {
        return Objects.requireNonNull(id, "Id must not be null");
    }

    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        return repository.save(checkEntityNotNull(configurationItem));
    }

    @Override
    public void delete(String id) {
        checkPresence(id, repository.delete(requireIdNotNull(id)));
    }

    @Override
    public ConfigurationItem get(String id) {
        return checkEntityPresence(id, repository.get(requireIdNotNull(id)));
    }
}
