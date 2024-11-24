package me.acp.tide.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a command handler.
 * This annotation is used to define commands in a declarative way without using the builder pattern.
 * 
 * Example usage:
 * ```java
 * // Synchronous command
 * @Command(
 *     name = "teleport",
 *     aliases = {"tp"},
 *     description = "Teleport to another player",
 *     permission = "example.teleport",
 *     usage = "/teleport <player>"
 * )
 * public void teleportCommand(CommandContext context) {
 *     // Command implementation
 * }
 * 
 * // Asynchronous command
 * @Command(
 *     name = "scan",
 *     description = "Scan a large area for resources",
 *     async = true
 * )
 * public void scanCommand(CommandContext context) {
 *     // Long-running operation that won't block the main thread
 *     for (int x = 0; x < 1000; x++) {
 *         for (int z = 0; z < 1000; z++) {
 *             // Process blocks
 *         }
 *     }
 * }
 * ```
 * 
 * The annotated method must accept a single {@link me.acp.tide.core.CommandContext} parameter.
 * 
 * IMPORTANT: When using async = true:
 * 1. Do NOT modify the world or entities directly from the async thread
 * 2. Use Bukkit.getScheduler().runTask() for any operations that must be sync
 * 3. Be careful with shared resources and consider thread safety
 * 4. Ideal for database operations, HTTP requests, and heavy computations
 * 
 * @since 1.0.0
 * @see me.acp.tide.core.CommandContext
 * @see Subcommand
 * @see TabCompleter
 * @see org.bukkit.scheduler.BukkitScheduler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * The name of the command.
     * This is what players will type after the forward slash to execute the command.
     * For example, if name is "teleport", players would type "/teleport".
     * 
     * @return The command's name
     */
    String name();

    /**
     * Alternative names for this command.
     * Players can use any of these aliases instead of the main command name.
     * For example, {"tp"} as an alias for "teleport" allows both "/teleport" and "/tp".
     * 
     * @return Array of command aliases, empty by default
     */
    String[] aliases() default {};

    /**
     * A brief description of what the command does.
     * This will be shown in help menus and command lists.
     * Should be concise but informative.
     * 
     * @return The command's description, empty by default
     */
    String description() default "";

    /**
     * The permission required to use this command.
     * Players without this permission will be unable to execute the command.
     * Leave empty to allow all players to use the command.
     * 
     * @return The permission node, empty by default
     */
    String permission() default "";

    /**
     * The usage message shown when the command is used incorrectly.
     * Should show the correct syntax, for example: "/command <required> [optional]"
     * Color codes using '&' are supported.
     * 
     * @return The usage message, empty by default
     */
    String usage() default "";

    /**
     * Whether this command should be executed asynchronously.
     * If true, the command will run in a separate thread to avoid blocking the main server thread.
     * 
     * Async commands are ideal for:
     * - Database operations
     * - HTTP requests
     * - File I/O
     * - Heavy computations
     * - Long-running tasks
     * 
     * WARNING: When async is true:
     * 1. You cannot modify the world or entities directly
     * 2. Use Bukkit.getScheduler().runTask() for sync operations
     * 3. Be careful with shared resources
     * 4. Consider thread safety implications
     * 
     * @return true to run async, false to run sync (default)
     */
    boolean async() default false;
}
