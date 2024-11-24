package me.acp.tide.core;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the context in which a command is executed.
 * Provides access to the command sender, arguments, and utility methods.
 * This class encapsulates all the information needed during command execution.
 *
 * <p>
 * Example usage:
 * ```java
 * public void onCommand(CommandContext context) {
 *     if (!context.isPlayer()) {
 *         context.getSender().sendMessage("This command is only for players!");
 *         return;
 *     }
 *
 * <p>
 *     Player player = context.getPlayer();
 *     String targetName = context.getArg(0);
 * <p>
 *     if (targetName == null) {
 *         player.sendMessage("Please specify a target player!");
 *         return;
 *     }
 *     // Command logic here
 * }
 * ```
 * 
 * @since 1.0.0
 */
public class CommandContext {
    private final CommandSender sender;   // The entity that executed the command
    private final String label;           // The actual command label used (including aliases)
    private final String[] args;          // Array of command arguments

    /**
     * Creates a new command context with the specified sender, label, and arguments.
     * 
     * @param sender The entity that executed the command (player, console, etc.)
     * @param label The command label that was used (including aliases)
     * @param args The arguments provided with the command
     * @throws IllegalArgumentException if any parameter is null
     */
    public CommandContext(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    /**
     * Gets the entity that executed the command.
     * This could be a player, console, command block, etc.
     *
     * @return The command sender, never null
     * @see org.bukkit.command.CommandSender
     */
    public @NotNull CommandSender getSender() {
        return sender;
    }

    /**
     * Gets the actual command label that was used.
     * This includes aliases. For example, if the command is registered as "teleport"
     * but the user typed "/tp", this would return "tp".
     *
     * @return The command label used, never null
     */
    public @NotNull String getLabel() {
        return label;
    }

    /**
     * Gets the array of command arguments.
     * Arguments are space-separated strings that come after the command label.
     * For example, in "/tp player world", ["player", "world"] would be the arguments.
     *
     * @return Array of command arguments, never null but may be empty
     */
    public @NotNull String[] getArgs() {
        return args;
    }

    /**
     * Gets the number of arguments provided with the command.
     * Useful for checking if enough arguments were provided.
     *
     * @return The number of arguments (0 if no arguments were provided)
     */
    public int getArgCount() {
        return args.length;
    }

    /**
     * Safely gets an argument at the specified index.
     * Returns null if the index is out of bounds, allowing for safe argument checking.
     *
     * @param index The index of the argument to get (0-based)
     * @return The argument at the specified index, or null if index is invalid
     */
    public @Nullable String getArg(int index) {
        return index >= 0 && index < args.length ? args[index] : null;
    }

    /**
     * Checks if the command sender is a player.
     * This is useful when a command should only be executed by players.
     *
     * @return true if the sender is a player, false if console or other sender
     * @see org.bukkit.entity.Player
     */
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    /**
     * Gets the command sender as a player if possible.
     * This is a safe way to get the player instance without casting.
     *
     * @return The sender as a Player if they are one, null otherwise
     * @see org.bukkit.entity.Player
     */
    public @Nullable Player getPlayer() {
        return isPlayer() ? (Player) sender : null;
    }
}
