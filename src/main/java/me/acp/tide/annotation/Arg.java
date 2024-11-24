package me.acp.tide.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter for automatic command argument injection.
 * This annotation enables automatic type conversion and validation of command arguments.
 * 
 * Example usage:
 * ```java
 * @Command(name = "give")
 * public void giveCommand(
 *     CommandSender sender,
 *     @Arg(name = "player", description = "Target player")
 *     Player target,
 *     @Arg(name = "item", description = "Item to give")
 *     Material item,
 *     @Arg(name = "amount", defaultValue = "1", required = false, description = "Amount to give")
 *     int amount
 * ) {
 *     // Command implementation
 *     target.getInventory().addItem(new ItemStack(item, amount));
 * }
 * ```
 * 
 * Supported parameter types:
 * - String: Raw argument text
 * - int/Integer: Numeric values
 * - double/Double: Decimal values
 * - boolean/Boolean: true/false values
 * - Player: Online player names
 * - OfflinePlayer: Any player that has joined
 * - World: Loaded world names
 * - Material: Valid material names
 * - Location: Coordinates (x,y,z) or (x,y,z,world)
 * 
 * @since 1.0.0
 * @see Command
 * @see Subcommand
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {
    /**
     * The name of the argument.
     * Used in error messages and help text to identify this argument.
     * For example, in "/give <player> <item> [amount]", "player", "item",
     * and "amount" would be the argument names.
     * 
     * @return The argument's name
     */
    String name();

    /**
     * The default value to use if the argument is not provided.
     * Only applicable if required is false.
     * Must be convertible to the parameter's type.
     * 
     * For example:
     * - Numbers: "1", "2.5"
     * - Booleans: "true", "false"
     * - Enums: The enum constant name
     * 
     * @return The default value, empty string if none
     */
    String defaultValue() default "";

    /**
     * Whether this argument must be provided.
     * If true, the command will fail if this argument is missing.
     * If false and the argument is missing, defaultValue will be used.
     * 
     * Required arguments should come before optional ones.
     * 
     * @return true if the argument is required, false if optional
     */
    boolean required() default true;

    /**
     * A brief description of what this argument is for.
     * Used in help text and error messages to explain the argument's purpose.
     * Should be concise but clear about what values are acceptable.
     * 
     * @return The argument's description, empty string if none
     */
    String description() default "";
}
