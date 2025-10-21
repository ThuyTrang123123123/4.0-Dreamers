package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class EventBus {
    private static final HashMap<String, List<Consumer<Object>>> listeners = new HashMap<>();

    public static void subscribe(String event, Consumer<Object> handler) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(handler);
    }

    public static void emit(String event, Object data) {
        List<Consumer<Object>> eventListeners = listeners.get(event);
        if (eventListeners != null) {
            for (Consumer<Object> handler : eventListeners) {
                handler.accept(data);
            }
        }
    }

    public static void clear() {
        listeners.clear();
    }
}
