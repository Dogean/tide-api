# Tab Completion Guide

This guide demonstrates various ways to implement tab completion using the Command API.

## Using Annotations

### Basic Tab Completion
```java
public class ExampleCommands {
    @Command(name = "gamemode", description = "Change gamemode")
    public void gamemodeCommand(CommandContext context) {
        // Command implementation
    }

    @TabCompleter(command = "gamemode")
    public List<String> onGamemodeTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("survival", "creative", "adventure", "spectator");
        }
        return Collections.emptyList();
    }
}
```

### Player Name Completion
```java
public class ExampleCommands {
    @Command(name = "teleport", description = "Teleport to a player")
    public void teleportCommand(CommandContext context) {
        // Command implementation
    }

    @TabCompleter(command = "teleport")
    public List<String> onTeleportTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
```

## Using Builder Pattern

### Basic Tab Completion
```java
CommandBuilder gamemodeCommand = CommandBuilder.create("gamemode")
    .description("Change gamemode")
    .tabCompleter((sender, args) -> {
        if (args.length == 1) {
            return Arrays.asList("survival", "creative", "adventure", "spectator")
                .stream()
                .filter(mode -> mode.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    })
    .executor(context -> {
        // Command implementation
    });
```

### Dynamic Tab Completion
```java
CommandBuilder kitCommand = CommandBuilder.create("kit")
    .description("Give a kit to a player")
    .tabCompleter((sender, args) -> {
        if (args.length == 1) {
            // Complete kit names
            return Arrays.asList("starter", "pvp", "builder")
                .stream()
                .filter(kit -> kit.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            // Complete player names
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    })
    .executor(context -> {
        // Command implementation
    });
```

### Subcommand Tab Completion
```java
CommandBuilder adminCommand = CommandBuilder.create("admin")
    .description("Admin commands")
    .subcommand(
        CommandBuilder.create("reload")
            .description("Reload configuration")
            .tabCompleter((sender, args) -> {
                if (args.length == 2) {
                    return Arrays.asList("config", "permissions", "all")
                        .stream()
                        .filter(option -> option.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
                }
                return Collections.emptyList();
            })
    )
    .subcommand(
        CommandBuilder.create("ban")
            .description("Ban a player")
            .tabCompleter((sender, args) -> {
                if (args.length == 2) {
                    return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
                }
                return Collections.emptyList();
            })
    );
```

## Best Practices

- Always filter suggestions based on what the user has already typed
```java
.filter(suggestion -> suggestion.toLowerCase().startsWith(args[argIndex].toLowerCase()))
```

- Handle null arguments gracefully
```java
if (args == null || args.length == 0) {
    return Collections.emptyList();
}
```

- Consider permissions when providing suggestions
```java
if (!sender.hasPermission("example.command")) {
    return Collections.emptyList();
}
```

- Provide suggestions based on previous arguments
```java
if (args.length == 2 && args[0].equalsIgnoreCase("teleport")) {
    return getPlayerNames();
} else if (args.length == 2 && args[0].equalsIgnoreCase("world")) {
    return getWorldNames();
}
```

- Cache frequently used suggestions
```java
private static final List<String> GAMEMODE_SUGGESTIONS = 
    Arrays.asList("survival", "creative", "adventure", "spectator");

@TabCompleter(command = "gamemode")
public List<String> onGamemodeTabComplete(CommandSender sender, String[] args) {
    if (args.length == 1) {
        return GAMEMODE_SUGGESTIONS;
    }
    return Collections.emptyList();
}
```
