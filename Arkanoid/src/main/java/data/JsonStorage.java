
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
}