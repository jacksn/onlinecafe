package test.onlinecafe.service;

import test.onlinecafe.model.ConfigurationItem;

public interface ConfigurationService {
    ConfigurationItem save(ConfigurationItem configurationItem);

    void delete(String id);

    ConfigurationItem get(String id);

}
