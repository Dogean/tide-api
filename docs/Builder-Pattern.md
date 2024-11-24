# Builder Pattern Commands in Tide

Tide provides a fluent builder API for creating commands programmatically. This approach offers more flexibility and runtime configuration compared to annotation-based commands.

## Basic Usage

```java
CommandBuilder.create("teleport")
    .description("Teleport to another player")
    .permission("example.teleport")
    .usage("/teleport <player>")
    .executor(context -> {
        if (!context.isPlayer()) {
            context.getSender().sendMessage("This command is only for players!");
            return;
        }
        
        String targetName = context.getArg(0);
        if (targetName == null) {
            context.getSender().sendMessage(context.getUsage());
            return;
        }
        
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            context.getSender().sendMessage("Player not found: " + targetName);
            return;
        }
        
        context.getPlayer().teleport(target);
        context.getSender().sendMessage("Teleported to " + target.getName());
    })
    .register(commandManager);
```

## Builder Methods

### Core Methods
- `create(String name)`: Create a new command builder
- `executor(Consumer<CommandContext>)`: Set the command execution logic
- `permission(String)`: Set required permission
- `description(String)`: Set command description
- `usage(String)`: Set usage message
- `aliases(String...)`: Set command aliases

### Subcommands
```java
CommandBuilder.create("admin")
    .description("Administrative commands")
    .permission("admin.use")
    .executor(context -> {
        context.getSender().sendMessage("Use /admin <reload|ban>");
    })
    .subcommand(
        CommandBuilder.create("reload")
            .description("Reload the plugin")
            .permission("admin.reload")
            .executor(context -> {
                // Reload logic
            })
    )
    .register(commandManager);
```

### Tab Completion
```java
CommandBuilder.create("gamemode")
    .description("Change gamemode")
    .permission("example.gamemode")
    .tabCompleter((sender, args) -> {
        if (args.length == 1) {
            return Arrays.asList("survival", "creative", "adventure", "spectator")
                .stream()
                .filter(mode -> mode.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    })
    .executor(context -> {
        // Command logic
    })
    .register(commandManager);
```

## Command Registration

Commands must be registered with the CommandManager:

```java
public class MyPlugin extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);

        // Register commands
        CommandBuilder.create("example")
            .description("Example command")
            .executor(context -> {
                // Command logic
            })
            .register(commandManager);
    }
}
```

## Advanced Examples

### Command with Multiple Subcommands
```java
CommandBuilder.create("economy")
    .description("Economy management commands")
    .permission("economy.use")
    .executor(context -> {
        context.getSender().sendMessage("Economy Commands:");
        context.getSender().sendMessage("/economy give <player> <amount>");
        context.getSender().sendMessage("/economy take <player> <amount>");
        context.getSender().sendMessage("/economy set <player> <amount>");
    })
    .subcommand(
        CommandBuilder.create("give")
            .description("Give money to a player")
            .permission("economy.give")
            .executor(context -> {
                // Give money logic
            })
    )
    .subcommand(
        CommandBuilder.create("take")
            .description("Take money from a player")
            .permission("economy.take")
            .executor(context -> {
                // Take money logic
            })
    )
    .subcommand(
        CommandBuilder.create("set")
            .description("Set a player's balance")
            .permission("economy.set")
            .executor(context -> {
                // Set balance logic
            })
    )
    .register(commandManager);
```

### Command with Complex Tab Completion
```java
CommandBuilder.create("warp")
    .description("Teleport to a warp point")
    .permission("warp.use")
    .tabCompleter((sender, args) -> {
        if (args.length == 1) {
            // List available warps based on permissions
            List<String> warps = getWarps();
            return warps.stream()
                .filter(warp -> sender.hasPermission("warp." + warp))
                .filter(warp -> warp.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    })
    .executor(context -> {
        // Warp command logic
    })
    .register(commandManager);
```

### Command with Argument Validation
```java
CommandBuilder.create("kit")
    .description("Give a kit to a player")
    .permission("kit.use")
    .executor(context -> {
        if (!context.isPlayer()) {
            context.getSender().sendMessage("This command is only for players!");
            return;
        }

        String kitName = context.getArg(0);
        if (kitName == null) {
            context.getSender().sendMessage("Usage: /kit <name>");
            return;
        }

        Kit kit = getKit(kitName);
        if (kit == null) {
            context.getSender().sendMessage("Kit not found: " + kitName);
            return;
        }

        if (!context.getSender().hasPermission("kit." + kitName)) {
            context.getSender().sendMessage("You don't have permission to use this kit!");
            return;
        }

        // Give kit to player
        kit.give(context.getPlayer());
        context.getSender().sendMessage("Given kit: " + kitName);
    })
    .register(commandManager);