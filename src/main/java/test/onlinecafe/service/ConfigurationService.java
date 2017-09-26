package test.onlinecafe.service;

public interface ConfigurationService {
    String save(String id, String value);

    void delete(String id);

    String get(String id);

}
