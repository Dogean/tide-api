package me.acp.tide.api;

import me.acp.tide.core.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Represents a command in the command system.
 * This interface defines the basic structure and behavior of a command.
 */
public interface ICommand {
    /**
     * Gets the name of the command.
     *
     * @return The command name
     */
    @NotNull String getName();

    /**
     * Gets the permission required to use this command.
     *
     * @return The permission string, or null if no permission is required
     */
    @Nullable String getPermission();

    /**
     * Gets the description of what this command does.
     *
     * @return The command description
     */
    @Nullable String getDescription();

    /**
     * Gets the list of aliases for this command.
     *
     * @return List of command aliases
     */
    @NotNull List<String> getAliases();

    /**
     * Gets the usage message for this command.
     *
     * @return The usage message
     */
    @Nullable String getUsage();

    /**
     * Gets the executor for this command.
     *
     * @return The command executor
     */
    @Nullable Consumer<CommandContext> getExecutor();

    /**
     * Gets the tab completer for this command.
     *
     * @return The tab completer
     */
    @Nullable BiPredicate<CommandSender, String[]> getTabCompleter();

    /**
     * Gets the list of subcommands for this command.
     *
     * @return List of subcommands
     */
    @NotNull List<? extends ICommand> getSubcommands();
}
