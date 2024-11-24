# Annotation-Based Commands in Tide

Tide provides a powerful annotation-based command system that allows you to define commands declaratively using annotations. This guide explains how to create commands using annotations and how to handle command arguments effectively.

## Basic Command Structure

### Simple Command
```java
@Command(
    name = "heal",
    description = "Heal a player",
    permission = "example.heal"
)
public void healCommand(CommandContext context) {
    if (!context.isPlayer()) {
        context.getSender().sendMessage("This command is only for players!");
        return;
    }
    Player player = context.getPlayer();
    player.setHealth(player.getMaxHealth());
    player.sendMessage("You have been healed!");
}
```

### Command with Arguments
```java
@Command(
    name = "give",
    description = "Give items to a player",
    usage = "/give <player> <item> [amount]"
)
public void giveCommand(
    CommandContext context,
    @Arg(name = "player", description = "Target player")
    Player target,
    @Arg(name = "item", description = "Item to give")
    Material item,
    @Arg(name = "amount", defaultValue = "1", required = false, description = "Amount to give")
    int amount
) {
    target.getInventory().addItem(new ItemStack(item, amount));
    context.getSender().sendMessage("Gave " + amount + " " + item.name() + " to " + target.getName());
}
```

## Argument System

Tide features a robust argument system using the `@Arg` annotation. This system provides:
- Automatic type conversion
- Required/optional argument handling
- Default values
- Descriptive error messages

### @Arg Annotation
The `@Arg` annotation has the following attributes:
- `name`: Argument name for error messages and help text
- `description`: Brief description of the argument's purpose
- `required`: Whether the argument must be provided (default: true)
- `defaultValue`: Default value if argument is optional and not provided

### Supported Parameter Types
- `String`: Raw argument text
- `int/Integer`: Numeric values
- `double/Double`: Decimal values
- `boolean/Boolean`: true/false values
- `Player`: Online player names
- `Material`: Valid material names

### Argument Handling Examples

```java
// Required arguments
@Command(name = "teleport")
public void teleportCommand(
    @Arg(name = "player", description = "Player to teleport to")
    Player target
) {
    // Command implementation
}

// Optional arguments with defaults
@Command(name = "feed")
public void feedCommand(
    @Arg(name = "player", required = false, defaultValue = "self", description = "Player to feed")
    String targetName,
    @Arg(name = "amount", required = false, defaultValue = "20", description = "Food level")
    int foodLevel
) {
    // Command implementation
}

// Mixed required and optional
@Command(name = "gamemode")
public void gamemodeCommand(
    @Arg(name = "mode", description = "Gamemode to set")
    GameMode mode,
    @Arg(name = "player", required = false, description = "Target player")
    Player target
) {
    // Command implementation
}
```

## Subcommands

Subcommands are defined using the `@Subcommand` annotation and must specify their parent command:

```java
@Command(name = "admin")
public class AdminCommands {
    @Subcommand(
        name = "reload",
        parent = "admin",
        description = "Reload the plugin configuration",
        permission = "admin.reload"
    )
    public void reloadCommand(
        @Arg(name = "type", description = "What to reload", defaultValue = "all")
        String type
    ) {
        // Subcommand implementation
    }
}
```

## Tab Completion

Tab completion is handled using the `@TabCompleter` annotation:

```java
@Command(name = "gamemode")
public class GamemodeCommand {
    @TabCompleter(command = "gamemode")
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("survival", "creative", "adventure", "spectator")
                .stream()
                .filter(mode -> mode.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
```

## Asynchronous Commands

Commands can be executed asynchronously using the `async` attribute:

```java
@Command(
    name = "scan",
    description = "Scan a large area",
    async = true
)
public void scanCommand(CommandContext context) {
    // This runs in a separate thread
    for (int x = 0; x < 1000; x++) {
        for (int z = 0; z < 1000; z++) {
            // Long-running operation
        }
    }
    
    // Use scheduler for any Bukkit API calls
    Bukkit.getScheduler().runTask(plugin, () -> {
        // Sync operations here
    });
}
```

Important notes for async commands:
1. Do NOT modify the world or entities directly
2. Use `Bukkit.getScheduler().runTask()` for sync operations
3. Be careful with shared resources
4. Consider thread safety implications

## Command Registration

Register your command classes with Tide:

```java
public class MyPlugin extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = new CommandManager(this);
        commandManager.registerCommands(new MyCommands());
    }
}
```