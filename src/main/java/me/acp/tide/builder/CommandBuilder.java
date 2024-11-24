package me.acp.tide.builder;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.acp.tide.api.ICommand;
import me.acp.tide.core.CommandContext;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Default implementation of the command builder.
 *
 * <p>
 * Example usage:
 * ```java
 * CommandBuilder.create("teleport")
 *     .description("Teleport to another player")
 *     .permission("example.teleport")
 *     .executor(context -> {
 *         // Command execution logic
 *     })
 *     .build();
 * ```
 * 
 * @since 1.0.0
 * @see AbstractCommandBuilder
 * @see ICommand
 */
public class CommandBuilder extends AbstractCommandBuilder {
    
    /**
     * Private constructor to enforce the use of the create() factory method.
     * 
     * @param name The name of the command, must not be null
     */
    private CommandBuilder(@NotNull String name) {
        super(name);
    }

    /**
     * Creates a new CommandBuilder instance with the specified name.
     * This is the entry point for creating new commands.
     * 
     * @param name The name of the command, what players will type after '/'
     * @return A new CommandBuilder instance
     * @throws IllegalArgumentException if name is null or empty
     */
    public static CommandBuilder create(@NotNull String name) {
        return new CommandBuilder(name);
    }

    /**
     * Sets the permission required to use this command.
     * Players without this permission will be unable to execute the command.
     * 
     * @param permission The permission node, or null to allow all players
     * @return This builder instance for method chaining
     */
    @Override
    public CommandBuilder permission(@Nullable String permission) {
        return (CommandBuilder) super.permission(permission);
    }

    /**
     * Sets the description of what this command does.
     * This will be shown in help menus and command lists.
     * 
     * @param description A brief description of the command's purpose
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if description is null
     */
    @Override
    public CommandBuilder description(@NotNull String description) {
        return (CommandBuilder) super.description(description);
    }

    /**
     * Sets alternative names for this command.
     * For example, "tp" might be an alias for "teleport".
     * 
     * @param aliases Array of alternative command names
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if aliases array is null
     */
    @Override
    public CommandBuilder aliases(@NotNull String... aliases) {
        return (CommandBuilder) super.aliases(aliases);
    }

    /**
     * Sets the usage message shown when the command is used incorrectly.
     * Should show the correct syntax, e.g. "/command <required> [optional]"
     * 
     * @param usage The usage message
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if usage is null
     */
    @Override
    public CommandBuilder usage(@NotNull String usage) {
        return (CommandBuilder) super.usage(usage);
    }

    /**
     * Sets the function that will be called when this command is executed.
     * The executor receives a CommandContext containing all relevant information.
     * 
     * @param executor The function to execute the command's logic
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if executor is null
     */
    @Override
    public CommandBuilder executor(@NotNull Consumer<CommandContext> executor) {
        return (CommandBuilder) super.executor(executor);
    }

    /**
     * Sets the function that provides tab completion suggestions.
     * This function is called when a player presses tab while typing the command.
     * 
     * @param tabCompleter Function that returns possible completions
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if tabCompleter is null
     */
    @Override
    public CommandBuilder tabCompleter(@NotNull BiPredicate<CommandSender, String[]> tabCompleter) {
        return (CommandBuilder) super.tabCompleter(tabCompleter);
    }

    /**
     * Adds a subcommand to this command.
     * Subcommands are executed when their name appears as the first argument.
     * 
     * @param subcommand The command to add as a subcommand
     * @return This builder instance for method chaining
     * @throws IllegalArgumentException if subcommand is null
     */
    @Override
    public CommandBuilder subcommand(@NotNull ICommand subcommand) {
        return (CommandBuilder) super.subcommand(subcommand);
    }
}
