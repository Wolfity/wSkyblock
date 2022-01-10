package me.wolf.wskyblock.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static String colorize(final String input) {
        return input == null ? "Null value" : ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> colorize(final List<String> list) {
        final List<String> coloredList = new ArrayList<>();
        for (final String s : list) {
            coloredList.add(colorize(s));
        }
        return coloredList;
    }

    public static String[] colorize(String... messages) {
        String[] colorized = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            colorized[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
        }
        return colorized;
    }

    public static boolean containsIgnoreCase(final String str, final String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

}
