package me.acp.tide.builder;

import me.acp.tide.CommandBuilder;
import me.acp.tide.api.ICommand;
import me.acp.tide.api.ICommandBuilder;
import me.acp.tide.core.AbstractCommand;
import me.acp.tide.core.CommandContext;
import me.acp.tide.core.CommandManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Abstract base implementation of ICommandBuilder that provides common builder functionality
 * for all command types. This class implements the Builder pattern to create command instances
 * in a fluent, type-safe way.
 * <p>
 * This class serves as the foundation for specific command builder implementations.
 * 
 * @since 1.0.0
 * @see ICommandBuilder
 * @see AbstractCommand
 * @see CommandBuilder
 */
public abstract class AbstractCommandBuilder extends AbstractCommand implements ICommandBuilder {
    
    /**
     * Creates a new command builder with the specified name.
     * This constructor initializes the basic command structure.
     * 
     * @param name The name of the command, must not be null
     * @throws IllegalArgumentException if name is null or empty
     */
    protected AbstractCommandBuilder(@NotNull String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     * Sets the permission required to use this command.
     * Null permission means anyone can use the command.
     * 
     * @param permission The permission node, or null to allow all players
     * @return This builder instance for method chaining
     */
    @Override
    public ICommandBuilder permission(@Nullable String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * {@inheritDoc}
     * Sets a description explaining what the command does.
     * The description is used in help menus and command listings.
     * 
     * @param description Brief explanation of the command's purpose
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if description is null
     */
    @Override
    public ICommandBuilder description(@NotNull String description) {
        this.description = description;
        return this;
    }

    /**
     * {@inheritDoc}
     * Adds alternative names that can be used to execute this command.
     * For example, adding "tp" as an alias for "teleport".
     * 
     * @param aliases Array of alternative command names
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if aliases array is null
     */
    @Override
    public ICommandBuilder aliases(@NotNull String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * {@inheritDoc}
     * Sets the usage message shown when the command is used incorrectly.
     * Should follow the format: "/command <required> [optional]"
     * 
     * @param usage The usage message showing correct command syntax
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if usage is null
     */
    @Override
    public ICommandBuilder usage(@NotNull String usage) {
        this.usage = usage;
        return this;
    }

    /**
     * {@inheritDoc}
     * Sets the function that executes the command's logic.
     * This function receives a CommandContext with all command information.
     * 
     * @param executor Function containing the command's execution logic
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if executor is null
     */
    @Override
    public ICommandBuilder executor(@NotNull Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    /**
     * {@inheritDoc}
     * Sets the function that provides tab completion suggestions.
     * Called when a player presses tab while typing the command.
     * 
     * @param tabCompleter Function returning possible command completions
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if tabCompleter is null
     */
    @Override
    public ICommandBuilder tabCompleter(@NotNull BiPredicate<CommandSender, String[]> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    /**
     * {@inheritDoc}
     * Adds a subcommand that can be executed as part of this command.
     * Subcommands are triggered when their name is the first argument.
     * 
     * Example: For a command "/admin reload", "reload" is a subcommand of "admin"
     * 
     * @param subcommand The command to add as a subcommand
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if subcommand is null
     */
    @Override
    public ICommandBuilder subcommand(@NotNull ICommand subcommand) {
        this.subcommands.add(subcommand);
        return this;
    }

    /**
     * {@inheritDoc}
     * Registers this command with the given CommandManager.
     * This is the final step in the builder chain that actually registers the command.
     *
     * @param commandManager The CommandManager to register with
     * @return The built command instance
     * @throws IllegalStateException if registration fails
     */
    @Override
    public ICommand register(@NotNull CommandManager commandManager) {
        commandManager.register(this);
        return this;
    }
}
