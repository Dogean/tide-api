package me.acp.tide.core;

import me.acp.tide.annotation.Arg;
import me.acp.tide.annotation.Command;
import me.acp.tide.annotation.Subcommand;
import me.acp.tide.annotation.TabCompleter;
import me.acp.tide.api.ICommand;
import me.acp.tide.CommandBuilder;
import me.acp.tide.util.ArgumentParser;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Core command management system for the Tide Command API.
 * Handles registration and execution of commands using both annotation-based and builder-based approaches.
 *
 * <p>
 * Example usage with annotations:
 * <p>
 * ```java
 * public class ExamplePlugin extends JavaPlugin {
 *     private CommandManager commandManager;
 * 
 *     @Override
 *     public void onEnable() {
 *         commandManager = new CommandManager(this);
 *         commandManager.registerCommands(new MyCommands());
 *     }
 * }
 * <p>
 * public class MyCommands {
 *     @Command(name = "hello", description = "Sends a greeting")
 *     public void helloCommand(CommandContext context, 
 *                            @Arg(name = "player") String target) {
 *         // Command implementation
 *     }
 * }
 * ```
 * <p>
 * Example usage with builder:
 * ```java
 * CommandBuilder.create("hello")
 *     .description("Sends a greeting")
 *     .executor(context -> {
 *         // Command implementation
 *     })
 *     .register(commandManager);
 * ```
 * 
 * @since 1.0.0
 * @see Command
 * @see Subcommand
 * @see TabCompleter
 * @see CommandBuilder
 */
public class CommandManager {
    private final JavaPlugin plugin;
    private final CommandMap commandMap;
    private final Map<String, ICommand> commands = new HashMap<>();

    /**
     * Creates a new CommandManager instance.
     * 
     * @param plugin The JavaPlugin instance this manager belongs to
     * @throws IllegalStateException if the CommandMap cannot be accessed
     */
    public CommandManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(plugin.getServer());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to access CommandMap: " + e.getMessage());
            throw new IllegalStateException("Could not initialize CommandManager", e);
        }
    }

    /**
     * Registers all commands defined in an object using annotations.
     * Scans the object for methods annotated with @Command, @Subcommand, and @TabCompleter.
     * 
     * @param object The object containing annotated command methods
     * @throws IllegalStateException if command registration fails
     * @see Command
     * @see Subcommand
     * @see TabCompleter
     */
    public void registerCommands(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                registerCommandMethod(method, object);
            } else if (method.isAnnotationPresent(Subcommand.class)) {
                registerSubcommandMethod(method, object);
            } else if (method.isAnnotationPresent(TabCompleter.class)) {
                registerTabCompleterMethod(method, object);
            }
        }
    }

    /**
     * Registers a command method annotated with @Command.
     */
    private void registerCommandMethod(Method method, Object object) {
        Command command = method.getAnnotation(Command.class);
        CommandBuilder builder = CommandBuilder.create(command.name())
            .aliases(command.aliases())
            .description(command.description())
            .permission(command.permission())
            .usage(command.usage())
            .executor(context -> {
                try {
                    method.setAccessible(true);
                    if (command.async()) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            try {
                                invokeMethod(method, object, context);
                            } catch (Exception e) {
                                handleCommandException("async command", e);
                            }
                        });
                    } else {
                        invokeMethod(method, object, context);
                    }
                } catch (Exception e) {
                    handleCommandException("command", e);
                }
            });

        commands.put(command.name().toLowerCase(), builder);
        register(builder);
    }

    /**
     * Registers a subcommand method annotated with @Subcommand.
     */
    private void registerSubcommandMethod(Method method, Object object) {
        Subcommand subcommand = method.getAnnotation(Subcommand.class);
        ICommand parent = commands.get(subcommand.parent().toLowerCase());
        if (parent != null) {
            CommandBuilder subBuilder = CommandBuilder.create(subcommand.name())
                .aliases(subcommand.aliases())
                .description(subcommand.description())
                .permission(subcommand.permission())
                .usage(subcommand.usage())
                .executor(context -> {
                    try {
                        method.setAccessible(true);
                        invokeMethod(method, object, context);
                    } catch (Exception e) {
                        handleCommandException("subcommand", e);
                    }
                });
            ((CommandBuilder) parent).subcommand(subBuilder);
        }
    }

    /**
     * Registers a tab completer method annotated with @TabCompleter.
     */
    private void registerTabCompleterMethod(Method method, Object object) {
        TabCompleter tabCompleter = method.getAnnotation(TabCompleter.class);
        ICommand command = commands.get(tabCompleter.command().toLowerCase());
        if (command != null) {
            ((CommandBuilder) command).tabCompleter((sender, args) -> {
                try {
                    method.setAccessible(true);
                    return (boolean) method.invoke(object, sender, args);
                } catch (Exception e) {
                    handleCommandException("tab completer", e);
                    return false;
                }
            });
        }
    }

    /**
     * Handles exceptions thrown during command execution.
     */
    private void handleCommandException(String type, Exception e) {
        plugin.getLogger().severe("Error executing " + type + ": " + e.getMessage());
        if (e.getCause() != null) {
            e.getCause().printStackTrace();
        }
    }

    /**
     * Invokes a command method with the appropriate arguments.
     * Handles automatic type conversion and validation of command arguments.
     * 
     * @param method The command method to invoke
     * @param instance The object instance containing the method
     * @param context The command context
     * @throws Exception if argument parsing or method invocation fails
     */
    private void invokeMethod(Method method, Object instance, CommandContext context) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        int currentArg = 0;
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (param.getType() == CommandContext.class) {
                args[i] = context;
                continue;
            }
            
            if (param.getType() == CommandSender.class) {
                args[i] = context.getSender();
                continue;
            }
            
            Arg argAnnotation = param.getAnnotation(Arg.class);
            if (argAnnotation == null) {
                throw new IllegalArgumentException("Parameter " + param.getName() + " must have @Arg annotation");
            }
            
            String value;
            if (currentArg < context.getArgs().length) {
                value = context.getArgs()[currentArg++];
            } else if (!argAnnotation.defaultValue().isEmpty()) {
                value = argAnnotation.defaultValue();
            } else if (argAnnotation.required()) {
                throw new IllegalArgumentException("Missing required argument: " + argAnnotation.name());
            } else {
                args[i] = null;
                continue;
            }
            
            try {
                args[i] = ArgumentParser.parseArgument(param.getType(), value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid argument for " + argAnnotation.name() + ": " + value);
            }
        }
        
        method.invoke(instance, args);
    }

    /**
     * Registers a command with the server's command map.
     * Creates a new Bukkit command that delegates to our command implementation.
     * 
     * @param builder The command builder containing the command configuration
     * @throws IllegalStateException if the command map is not available
     */
    public void register(@NotNull ICommand builder) {
        if (commandMap == null) {
            throw new IllegalStateException("Cannot register command: CommandMap is null");
        }

        org.bukkit.command.Command command = new org.bukkit.command.Command(builder.getName()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                if (builder.getPermission() != null && !sender.hasPermission(builder.getPermission())) {
                    sender.sendMessage("§cYou don't have permission to use this command.");
                    return true;
                }

                if (args.length > 0) {
                    for (ICommand subcommand : builder.getSubcommands()) {
                        if (subcommand.getName().equalsIgnoreCase(args[0]) || 
                            subcommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(args[0]))) {
                            String[] newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                            return executeCommand(subcommand, sender, args[0], newArgs);
                        }
                    }
                }

                return executeCommand(builder, sender, label, args);
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
                if (args.length == 1) {
                    List<String> completions = new ArrayList<>();
                    for (ICommand subcommand : builder.getSubcommands()) {
                        if (subcommand.getPermission() == null || sender.hasPermission(subcommand.getPermission())) {
                            if (subcommand.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                                completions.add(subcommand.getName());
                            }
                            for (String subAlias : subcommand.getAliases()) {
                                if (subAlias.toLowerCase().startsWith(args[0].toLowerCase())) {
                                    completions.add(subAlias);
                                }
                            }
                        }
                    }
                    return completions;
                }

                if (builder.getTabCompleter() != null) {
                    return builder.getTabCompleter().test(sender, args) ? new ArrayList<>() : null;
                }

                return new ArrayList<>();
            }
        };

        command.setDescription(builder.getDescription() != null ? builder.getDescription() : "");
        command.setAliases(builder.getAliases());
        command.setUsage(builder.getUsage() != null ? builder.getUsage() : "");

        commandMap.register(plugin.getName().toLowerCase(), command);
    }

    /**
     * Executes a command with the given context.
     * Handles permission checks and command execution.
     * 
     * @param command The command to execute
     * @param sender The command sender
     * @param label The command label used
     * @param args The command arguments
     * @return true if the command was handled, false otherwise
     */
    private boolean executeCommand(ICommand command, CommandSender sender, String label, String[] args) {
        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        if (command.getExecutor() != null) {
            command.getExecutor().accept(new CommandContext(sender, label, args));
            return true;
        }

        return false;
    }
}
