package test.onlinecafe.service;

import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;
import test.onlinecafe.util.exception.NotFoundException;

import java.util.Objects;

import static test.onlinecafe.util.ValidationUtil.checkPresence;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConfigurationItem save(ConfigurationItem configurationItem) {
        return repository.save(Objects.requireNonNull(configurationItem, "Configuration item must not be null"));
    }

    @Override
    public void delete(String id) {
        boolean result = repository.delete(Objects.requireNonNull(id, "Id must not be null"));
        if (!result) {
            throw new NotFoundException("Configuration item with id \"" + id + "\" not found");
        }
    }

    @Override
    public ConfigurationItem get(String id) {
        ConfigurationItem item = repository.get(Objects.requireNonNull(id, "Id must not be null"));
        checkPresence(id, item);
        return item;
    }
}
