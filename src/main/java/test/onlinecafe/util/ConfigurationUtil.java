package test.onlinecafe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationUtil {
    private ConfigurationUtil() {
    }

    public static Properties getPropertiesFromFile(String fileName) throws IOException {
        try (InputStream inputStream = ConfigurationUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }
}
