package com.backend.server.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.yaml.snakeyaml.Yaml;

public class YamlUtil {

    private static final Map<String, Object> yamlData;

    static {
        Map<String, Object> data;
        try (InputStream input = YamlUtil.class.getClassLoader().getResourceAsStream("settings/settings.yml")) {
            if (input == null)
                throw new RuntimeException("settings.yml 파일을 찾을 수 없습니다.");
            Yaml yaml = new Yaml();
            data = yaml.load(input);
        } catch (Exception e) {
            data = new HashMap<>();
        }
        yamlData = data;
    }

    private static Object get(String key) {
        return yamlData.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getNested(String... keys) {
        Map<String, Object> current = yamlData;
        Object value = null;

        try {
            for (int i = 0; i < keys.length; i++) {
                value = current.get(keys[i]);
                if (i == keys.length - 1)
                    return Optional.ofNullable((T) value);
                if (!(value instanceof Map))
                    return Optional.empty();
                current = (Map<String, Object>) value;
            }
            return Optional.empty();
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}