package org.quadrex.script.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.quadrex.script.QScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class QCommand implements CommandExecutor {

    private QScript script;
    private String name;
    private List<String> aliases;
    private Optional<String> permission;

    public QCommand(QScript script, String name, List<String> aliases, String permission) {
        this.script = script;
        this.name = name;
        this.aliases = aliases == null ? new ArrayList<>() : aliases;
        this.permission = Optional.ofNullable(permission);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    public QCommand(QScript script, String name) {
        this(script, name, null ,null);
    }

    public QCommand(QScript script, String name, List<String> aliases) {
        this(script, name, aliases, null);
    }

    public abstract void execute(CommandSender sender, String typedCommand, String[] args);

    public QScript getScript() {
        return script;
    }

    public boolean isAlias(String command) {
        return name.equalsIgnoreCase(command) || aliases.stream().anyMatch(command::equalsIgnoreCase);
    }

    public Optional<String> getPermission() {
        return permission;
    }

    public boolean permissionCheck(Player player) {
        return permission.map(player::hasPermission).orElse(true);
    }
}