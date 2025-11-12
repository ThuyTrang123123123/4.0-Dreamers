package data;

import java.util.List;
import java.util.Map;

public interface Storage {

    void save(String key, Map<String, Object> data);

    Map<String, Object> load(String key);

    void saveList(String key, List<Map<String, Object>> list);

    List<Map<String, Object>> loadList(String key);

    void delete(String key);

    void putInt(String key, int value);

    int getInt(String key, int def);

    void putString(String key, String value);

    String getString(String key, String def);

    void flush();
}