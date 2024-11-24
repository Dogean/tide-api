package me.acp.tide.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a tab completion handler for a command.
 * Tab completion provides suggestions when a player presses tab while typing a command.
 * 
 * Example usage:
 * ```java
 * @Command(name = "gamemode")
 * public class GamemodeCommand {
 *     @TabCompleter(command = "gamemode")
 *     public List<String> onGamemodeTabComplete(CommandSender sender, String[] args) {
 *         if (args.length == 1) {
 *             return Arrays.asList("survival", "creative", "adventure", "spectator")
 *                 .stream()
 *                 .filter(mode -> mode.startsWith(args[0].toLowerCase()))
 *                 .collect(Collectors.toList());
 *         }
 *         return Collections.emptyList();
 *     }
 * }
 * ```
 * 
 * The annotated method must have the following signature:
 * ```java
 * List<String> methodName(CommandSender sender, String[] args)
 * ```
 * 
 * @since 1.0.0
 * @see Command
 * @see Subcommand
 * @see org.bukkit.command.CommandSender
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TabCompleter {
    /**
     * The name of the command this tab completer is for.
     * This must match the name of a command annotated with @Command
     * or a subcommand annotated with @Subcommand.
     * 
     * For subcommands, use the parent command name followed by a dot
     * and the subcommand name. For example: "admin.reload"
     * 
     * @return The command name this tab completer is associated with
     */
    String command();
}
