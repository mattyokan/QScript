package org.quadrex.script.core.model;

import org.quadrex.script.QScript;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.command.CommandManager;
import org.quadrex.script.api.command.QCommand;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitCommandManager implements CommandManager {

    private ScriptPlugin plugin;

    private Map<String, QCommand> commands;

    public BukkitCommandManager(ScriptPlugin plugin) {
        this.plugin = plugin;
        commands = new ConcurrentHashMap<>();
    }

    @Override
    public void registerCommand(String name, QCommand command) {
        commands.put(name.toLowerCase(), command);
    }

    @Override
    public void unregisterCommand(QCommand command) {
        commands.entrySet().stream().filter(e -> e.getValue() != null).filter(entry -> entry.getValue().equals(command)).forEach(entry -> commands.remove(entry.getKey()));
    }

    @Override
    public void unregisterCommand(String name) {
        commands.remove(name);
    }

    @Override
    public void unregisterAll(QScript script) {
        commands.entrySet().stream().filter(e -> e != null && e.getKey() != null && e.getValue() != null).filter(entry -> entry.getValue().getScript().equals(script)).forEach(entry -> commands.remove(entry.getKey()));
    }

    @Override
    public Optional<QCommand> getCommand(String label) {
        return commands.values().stream().filter(q -> q.isAlias(label)).findAny();
    }

}
