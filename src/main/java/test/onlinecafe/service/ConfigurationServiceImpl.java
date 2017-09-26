package test.onlinecafe.service;

import org.springframework.stereotype.Service;
import test.onlinecafe.model.ConfigurationItem;
import test.onlinecafe.repository.ConfigurationRepository;

import static java.util.Objects.requireNonNull;
import static test.onlinecafe.util.ValidationUtil.checkEntityPresence;
import static test.onlinecafe.util.ValidationUtil.checkPresence;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    public static final String MESSAGE_ID_MUST_NOT_BE_NULL = "Id must not be null.";
    public static final String MESSAGE_VALUE_MUST_NOT_BE_NULL = "Value must not be null.";
    private final ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        this.repository = repository;
    }

    private static String requireNotNullId(String id) {
        return requireNonNull(id, MESSAGE_ID_MUST_NOT_BE_NULL);
    }

    @Override
    public String save(String id, String value) {
        ConfigurationItem item = new ConfigurationItem(requireNonNull(id, MESSAGE_ID_MUST_NOT_BE_NULL),
                requireNonNull(value, MESSAGE_VALUE_MUST_NOT_BE_NULL));
        return repository.save(item).getValue();
    }

    @Override
    public void delete(String id) {
        requireNotNullId(id);
        checkPresence(id, repository.delete(id));
    }

    @Override
    public String get(String id) {
        return checkEntityPresence(id, repository.get(requireNonNull(id, MESSAGE_ID_MUST_NOT_BE_NULL))).getValue();
    }
}
