
package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStorage implements Storage {
    private static final String DATA_DIR = "src/main/resources/data/";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final JsonStorage INSTANCE = new JsonStorage();
    private final Map<String, Object> cache = new HashMap<>();

    public static JsonStorage getInstance() {
        return INSTANCE;
    }

    public JsonStorage() {
        // Đảm bảo thư mục tồn tại
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();

        // Tải cache từ file chính (data.json)
        File mainFile = new File(DATA_DIR + "data.json");
        if (mainFile.exists()) {
            try {
                Map<?, ?> loaded = mapper.readValue(mainFile, HashMap.class);
                for (Map.Entry<?, ?> e : loaded.entrySet()) {
                    cache.put(String.valueOf(e.getKey()), e.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(String key, Map<String, Object> data) {
        try {
            mapper.writeValue(new File(DATA_DIR + key + ".json"), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> load(String key) {
        try {
            File file = new File(DATA_DIR + key + ".json");
            if (file.exists()) {
                return mapper.readValue(file, HashMap.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    public void saveList(String key, List<Map<String, Object>> list) {
        try {
            mapper.writeValue(new File(DATA_DIR + key + ".json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> loadList(String key) {
        try {
            File file = new File(DATA_DIR + key + ".json");
            if (file.exists()) {
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, HashMap.class);
                return mapper.readValue(file, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(String key) {
        File file = new File(DATA_DIR + key + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void putInt(String key, int value) {
        cache.put(key, value);
        saveAll();
    }

    @Override
    public int getInt(String key, int def) {
        Object v = cache.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return def;
        }
    }

    @Override
    public void putString(String key, String value) {
        cache.put(key, value);
        saveAll();
    }

    @Override
    public String getString(String key, String def) {
        Object v = cache.get(key);
        return (v != null) ? String.valueOf(v) : def;
    }

    @Override
    public void flush() {
        saveAll();
    }

    private void saveAll() {
        try {
            mapper.writeValue(new File(DATA_DIR + "data.json"), cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}