# Tide

A flexible and intuitive command framework for Spigot plugins, inspired by drink.

## Features

- Fluent command builder API
- Annotation-based command registration
- Subcommand support
- Permission handling
- Tab completion
- Argument parsing utilities
- Type-safe command context
- Automatic parameter injection

## Project Structure

The project is organized into the following packages:

### `api` - Public Interfaces
- `ICommand` - Base interface for all commands
- `ICommandBuilder` - Interface for the command builder pattern

### `builder` - Builder Pattern Implementations
- `AbstractCommandBuilder` - Base builder implementation
- `CommandBuilder` - Default command builder implementation

### `core` - Core Implementation Classes
- `AbstractCommand` - Base command implementation
- `CommandContext` - Command execution context

### `util` - Utility Classes
- `ArgumentParser` - Argument parsing utilities

### `annotation` - Command Annotations
- `@Command` - Define main commands
- `@Subcommand` - Define subcommands
- `@TabCompleter` - Define tab completers
- `@Arg` - Automatic parameter injection

#### `CommandManager` - Main command management


## Usage

You can use this API in two ways: using annotations or using the builder pattern.

### Annotation-based Commands

```java

public class ExamplePlugin extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);

        // Register commands
        commandManager.registerCommands(new ExampleCommands());
    }
}

// Example implementation of annotated commands
public class ExampleCommands {
    @Command(name = "hello", description = "Sends a hello message")
    public void helloCommand(CommandContext context) {
        if (context.isPlayer()) {
            context.getPlayer().sendMessage("Hello, " + context.getPlayer().getName() + "!");
        } else {
            context.getSender().sendMessage("Hello, Console!");
        }
    }

    @Command(name = "admin", description = "Admin commands", permission = "example.admin")
    @Subcommand(name = "reload", description = "Reloads the plugin", permission = "example.admin.reload")
    public void reloadCommand(CommandContext context) {
        // Reload logic here
        context.getSender().sendMessage("Plugin reloaded!");
    }

    @Command(name = "give", description = "Gives an item to a player")
    public void giveCommand(CommandSender sender, @Arg(name = "player") Player target, @Arg(name = "amount", defaultValue = "1") int amount) {
        // Command logic with automatic parameter injection
        sender.sendMessage("Gave " + amount + " items to " + target.getName());
    }

    @TabCompleter(command = "give")
    public boolean onGiveTabComplete(CommandSender sender, String[] args) {
        // Tab completion logic
        return true;
    }
}
```

### Builder Pattern

```java
public class ExamplePlugin extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);

        // Create a command using builder pattern
        CommandBuilder simpleCommand = CommandBuilder.create("hello")
            .description("Sends a hello message")
            .executor(ctx -> {
                if (ctx.isPlayer()) {
                    ctx.getPlayer().sendMessage("Hello, " + ctx.getPlayer().getName() + "!");
                } else {
                    ctx.getSender().sendMessage("Hello, Console!");
                }
            }).register(commandManager);
        

        // Create a command with subcommands
        CommandBuilder complexCommand = CommandBuilder.create("admin")
            .permission("example.admin")
            .description("Admin commands")
            .subcommand(
                CommandBuilder.create("reload")
                    .permission("example.admin.reload")
                    .description("Reloads the plugin")
                    .executor(ctx -> {
                        // Reload logic here
                        ctx.getSender().sendMessage("Plugin reloaded!");
                    })
            )
            .subcommand(
                CommandBuilder.create("broadcast")
                    .permission("example.admin.broadcast")
                    .description("Broadcasts a message")
                    .executor(ctx -> {
                        if (ctx.getArgCount() < 1) {
                            ctx.getSender().sendMessage("Usage: /admin broadcast <message>");
                            return;
                        }
                        String message = String.join(" ", ctx.getArgs());
                        getServer().broadcastMessage(message);
                    })
            ).register(commandManager);

        // Register commands
    }
}
```

## Installation

1. Add this repository to your project dependencies
2. Create an instance of `CommandManager` in your plugin
3. Use either annotations or `CommandBuilder` to create and register your commands

## Features

### Annotation Support
- `@Command` - Define main commands
- `@Subcommand` - Define subcommands
- `@TabCompleter` - Define tab completers
- `@Arg` - Automatic parameter injection with type conversion

### Command Builder
- Fluent API for creating commands
- Support for permissions
- Custom executors
- Tab completion
- Subcommand support

### Command Context
- Easy access to command sender
- Type-safe argument parsing
- Utility methods for common operations

### Command Manager
- Simple command registration
- Automatic permission handling
- Subcommand routing
- Tab completion support
- Support for both annotation and builder patterns

## Docs
More documentation is found in [docs](docs).

## Contributing

Feel free to submit issues and pull requests to improve this API.
