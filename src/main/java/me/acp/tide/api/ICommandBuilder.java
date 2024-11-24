package me.acp.tide.api;

import me.acp.tide.core.CommandContext;
import me.acp.tide.core.CommandManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Builder interface for creating commands.
 * Provides a fluent API for command construction.
 */
public interface ICommandBuilder extends ICommand {
    /**
     * Sets the permission required to use this command.
     *
     * @param permission The permission string
     * @return This builder instance
     */
    ICommandBuilder permission(@Nullable String permission);

    /**
     * Sets the description of what this command does.
     *
     * @param description The command description
     * @return This builder instance
     */
    ICommandBuilder description(@NotNull String description);

    /**
     * Sets the aliases for this command.
     *
     * @param aliases The command aliases
     * @return This builder instance
     */
    ICommandBuilder aliases(@NotNull String... aliases);

    /**
     * Sets the usage message for this command.
     *
     * @param usage The usage message
     * @return This builder instance
     */
    ICommandBuilder usage(@NotNull String usage);

    /**
     * Sets the executor for this command.
     *
     * @param executor The command executor
     * @return This builder instance
     */
    ICommandBuilder executor(@NotNull Consumer<CommandContext> executor);

    /**
     * Sets the tab completer for this command.
     *
     * @param tabCompleter The tab completer
     * @return This builder instance
     */
    ICommandBuilder tabCompleter(@NotNull BiPredicate<CommandSender, String[]> tabCompleter);

    /**
     * Adds a subcommand to this command.
     *
     * @param subcommand The subcommand to add
     * @return This builder instance
     */
    ICommandBuilder subcommand(@NotNull ICommand subcommand);

    /**
     * Registers this command with the given CommandManager.
     * This is the final step in the builder chain that actually registers the command.
     *
     * @param commandManager The CommandManager to register with
     * @return The built command instance
     * @throws IllegalStateException if registration fails
     */
    ICommand register(@NotNull CommandManager commandManager);
}
