package ui.theme;

import data.JsonStorage;
import data.Storage;

public final class ThemeManager {
    private static final String KEY_LEVEL_PREFIX = "theme.level.";

    private ThemeManager(){}

    private static Storage store() { return JsonStorage.getInstance(); }

    public static void setThemeForLevel(int level, String kind) {
        if (!"C".equals(kind) && !"T".equals(kind)) return;
        store().putString(KEY_LEVEL_PREFIX + level, kind);
        store().flush();
    }

    public static String getThemeForLevel(int level) {
        try {
            String v = store().getString(KEY_LEVEL_PREFIX + level, null);

            if (v == null) return null;
            v = v.trim();
            if (v.isEmpty() || "null".equalsIgnoreCase(v)) return null;
            return v;
        } catch (Exception ignore) { return null; }
    }

    public static String pathForSelectedTheme(int level) {
        String k = getThemeForLevel(level);
        if (k == null) return null;
        return pathForKind(level, k);
    }

    public static String pathForKind(int level, String kind) {
        String prefix = "T".equals(kind) ? "TLevel " : "CLevel ";
        return "/images/" + prefix + level + ".png";
    }
    public static void resetThemeForLevel(int level) {
        JsonStorage.getInstance().putString("theme.level." + level, null);
        JsonStorage.getInstance().flush();
    }
}