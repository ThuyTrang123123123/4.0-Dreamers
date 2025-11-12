package ui.theme;

import data.JsonStorage;
import data.Storage;

public final class ThemeManager {
    private static final String KEY_LEVEL_PREFIX = "theme.level.";

    private ThemeManager() {}

    private static Storage store() { return JsonStorage.getInstance(); }

    public static void setThemeForLevel(int level, String kind) {
        String k = normalizeKind(kind);
        if (k == null) return;
        store().putString(KEY_LEVEL_PREFIX + level, k);
        store().flush();
    }

    public static String getThemeForLevel(int level) {
        try {
            String v = store().getString(KEY_LEVEL_PREFIX + level, null);
            v = normalizeKind(v);
            return v;
        } catch (Exception ignore) {
            return null;
        }
    }

    public static String pathForSelectedTheme(int level) {
        String k = getThemeForLevel(level);
        if (k == null) return null;
        return pathForKind(level, k);
    }

    public static String pathForKind(int level, String kind) {
        String k = normalizeKind(kind);
        String prefix = "T".equals(k) ? "TLevel " : "CLevel ";
        return "/images/" + prefix + level + ".png";
    }

    public static void resetThemeForLevel(int level) {
        store().putString(KEY_LEVEL_PREFIX + level, null);
        store().flush();
    }

    public static String defaultPathForLevel(int level) {
        return "/images/Level " + level + ".png";
    }

    private static String normalizeKind(String k) {
        if (k == null) return null;
        k = k.trim().toUpperCase();
        if ("C".equals(k) || "T".equals(k)) return k;
        return null;
    }
}
