package me.acp.tide.core;

import me.acp.tide.api.ICommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Abstract base implementation of ICommand that provides common functionality
 * for all command types. This class serves as the foundation for command implementation,
 * providing storage and access to essential command properties.
 *
 * <p>
 * This class implements the core command functionality while leaving specific
 * behaviors to be defined by concrete implementations.
 * 
 * @since 1.0.0
 * @see ICommand
 * @see CommandContext
 */
public abstract class AbstractCommand implements ICommand {
    protected final String name;                                      // The command's name (immutable)
    protected String permission;                                      // Permission required to use the command
    protected String description;                                     // Description of what the command does
    protected final List<String> aliases;                             // Alternative command names
    protected Consumer<CommandContext> executor;                      // Function that executes the command
    protected BiPredicate<CommandSender, String[]> tabCompleter;      // Function for tab completion
    protected final List<ICommand> subcommands;                       // List of subcommands
    protected String usage;                                           // Usage message for the command

    /**
     * Creates a new command with the specified name.
     * Initializes empty lists for aliases and subcommands.
     * 
     * @param name The name of the command, must not be null
     * @throws IllegalArgumentException if name is null
     */
    protected AbstractCommand(@NotNull String name) {
        this.name = name;
        this.aliases = new ArrayList<>();
        this.subcommands = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * Gets the immutable name of this command.
     * This is what players type after the forward slash.
     * 
     * @return The command's name, never null
     */
    @Override
    public @NotNull String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * Gets the permission required to use this command.
     * Commands without a permission can be used by anyone.
     * 
     * @return The permission node, or null if no permission is required
     */
    @Override
    public @Nullable String getPermission() {
        return permission;
    }

    /**
     * {@inheritDoc}
     * Gets the description of what this command does.
     * Used in help menus and command listings.
     * 
     * @return The command's description, or null if none is set
     */
    @Override
    public @Nullable String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     * Gets the list of alternative names for this command.
     * For example, "tp" might be an alias for "teleport".
     * 
     * @return List of command aliases, never null but may be empty
     */
    @Override
    public @NotNull List<String> getAliases() {
        return aliases;
    }

    /**
     * {@inheritDoc}
     * Gets the usage message for this command.
     * Shows the correct syntax when the command is used incorrectly.
     * 
     * @return The usage message, or null if none is set
     */
    @Override
    public @Nullable String getUsage() {
        return usage;
    }

    /**
     * Gets the function that executes this command's logic.
     * The executor receives a CommandContext with all relevant information.
     * 
     * @return The command executor, or null if none is set
     * @see CommandContext
     */
    public @Nullable Consumer<CommandContext> getExecutor() {
        return executor;
    }

    /**
     * Gets the function that provides tab completion suggestions.
     * Called when a player presses tab while typing the command.
     * 
     * @return The tab completer function, or null if none is set
     */
    public @Nullable BiPredicate<CommandSender, String[]> getTabCompleter() {
        return tabCompleter;
    }

    /**
     * {@inheritDoc}
     * Gets the list of subcommands registered to this command.
     * Subcommands are triggered by their name as the first argument.
     * 
     * @return List of subcommands, never null but may be empty
     */
    @Override
    public @NotNull List<? extends ICommand> getSubcommands() {
        return subcommands;
    }
}
