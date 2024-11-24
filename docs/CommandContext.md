# CommandContext

The `CommandContext` class represents the context in which a command is executed in the Tide framework. It provides a clean and safe way to access command sender information, arguments, and utility methods for command execution.

## Overview

This class encapsulates all the information needed during command execution and provides several key features:
- Safe access to command sender and arguments
- Type-safe player checks and conversion
- Argument count and bounds checking
- Null-safe argument access

## Constructor

```java
public CommandContext(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args)
```

Creates a new command context with the specified parameters:
- `sender`: The entity that executed the command (player, console, etc.)
- `label`: The command label that was used (including aliases)
- `args`: The arguments provided with the command

Throws `IllegalArgumentException` if any parameter is null.

## Methods

### Command Sender Access

#### `getSender()`
```java
public @NotNull CommandSender getSender()
```
Returns the entity that executed the command (never null). This could be a player, console, command block, etc.

#### `isPlayer()`
```java
public boolean isPlayer()
```
Checks if the command sender is a player. Returns `true` if the sender is a player, `false` if console or other sender.

#### `getPlayer()`
```java
public @Nullable Player getPlayer()
```
Gets the command sender as a player if possible. Returns `null` if the sender is not a player.

### Command Information

#### `getLabel()`
```java
public @NotNull String getLabel()
```
Gets the actual command label that was used, including aliases. For example, if the command is registered as "teleport" but the user typed "/tp", this would return "tp".

### Argument Handling

#### `getArgs()`
```java
public @NotNull String[] getArgs()
```
Returns the array of command arguments. Arguments are space-separated strings that come after the command label.

#### `getArgCount()`
```java
public int getArgCount()
```
Returns the number of arguments provided with the command (0 if no arguments were provided).

#### `getArg(int index)`
```java
public @Nullable String getArg(int index)
```
Safely gets an argument at the specified index. Returns `null` if the index is out of bounds.

## Example Usage

```java
public void onCommand(CommandContext context) {
    // Check if sender is a player
    if (!context.isPlayer()) {
        context.getSender().sendMessage("This command is only for players!");
        return;
    }
    
    // Get the player instance
    Player player = context.getPlayer();
    
    // Safely get the first argument
    String targetName = context.getArg(0);
    
    if (targetName == null) {
        player.sendMessage("Please specify a target player!");
        return;
    }
    
    // Command logic here
}
```

## Version Information

- Since: 1.0.0
