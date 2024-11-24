package me.acp.tide.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a subcommand handler.
 * Subcommands are commands that are executed as part of a parent command.
 * They appear as the first argument after the main command.
 * 
 * Example usage:
 * ```java
 * @Command(name = "admin")
 * public class AdminCommands {
 *     @Subcommand(
 *         name = "reload",
 *         description = "Reload the plugin configuration",
 *         permission = "admin.reload",
 *         usage = "/admin reload [config|all]",
 *         parent = "admin"
 *     )
 *     public void reloadCommand(CommandContext context) {
 *         // Subcommand implementation
 *     }
 * }
 * ```
 * 
 * The annotated method must accept a single {@link me.acp.tide.core.CommandContext} parameter.
 * 
 * @since 1.0.0
 * @see Command
 * @see me.acp.tide.core.CommandContext
 * @see TabCompleter
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
    /**
     * The name of the subcommand.
     * This is what players will type after the parent command.
     * For example, if parent is "admin" and name is "reload",
     * players would type "/admin reload".
     * 
     * @return The subcommand's name
     */
    String name();

    /**
     * Alternative names for this subcommand.
     * Players can use any of these aliases instead of the main subcommand name.
     * For example, {"r"} as an alias for "reload" allows both
     * "/admin reload" and "/admin r".
     * 
     * @return Array of subcommand aliases, empty by default
     */
    String[] aliases() default {};

    /**
     * A brief description of what the subcommand does.
     * This will be shown in help menus and command lists.
     * Should be concise but informative.
     * 
     * @return The subcommand's description, empty by default
     */
    String description() default "";

    /**
     * The permission required to use this subcommand.
     * Players without this permission will be unable to execute the subcommand.
     * This is checked in addition to the parent command's permission.
     * Leave empty to use only the parent command's permission check.
     * 
     * @return The permission node, empty by default
     */
    String permission() default "";

    /**
     * The usage message shown when the subcommand is used incorrectly.
     * Should show the correct syntax, including the parent command.
     * For example: "/admin reload [config|all]"
     * Color codes using '&' are supported.
     * 
     * @return The usage message, empty by default
     */
    String usage() default "";

    /**
     * The name of the parent command this subcommand belongs to.
     * This must match the name of a command annotated with @Command.
     * The subcommand will only be available when used after this parent command.
     * 
     * @return The parent command's name
     */
    String parent();
}
