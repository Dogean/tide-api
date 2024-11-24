package me.acp.tide.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for parsing command arguments into their respective types.
 */
public class ArgumentParser {
    
    /**
     * Parses a string value into the specified type.
     *
     * @param type The target type to parse into
     * @param value The string value to parse
     * @return The parsed value
     * @throws IllegalArgumentException if the value cannot be parsed
     */
    public static Object parseArgument(@NotNull Class<?> type, @NotNull String value) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (type == Player.class) {
            Player player = Bukkit.getPlayer(value);
            if (player == null) {
                throw new IllegalArgumentException("Player not found: " + value);
            }
            return player;
        }
        throw new IllegalArgumentException("Unsupported argument type: " + type.getName());
    }
}
