package test.onlinecafe.repository;

import test.onlinecafe.model.ConfigurationItem;

public interface ConfigurationRepository {
    ConfigurationItem save(ConfigurationItem configurationItem);

    boolean delete(String id);

    ConfigurationItem get(String id);

}
