package data;

import java.util.List;
import java.util.Map;

public interface Storage {

    void save(String key, Map<String, Object> data);

    Map<String, Object> load(String key);

    void saveList(String key, List<Map<String, Object>> list);

    List<Map<String, Object>> loadList(String key);

    void delete(String key);
}