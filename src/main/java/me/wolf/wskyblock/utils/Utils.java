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

}
