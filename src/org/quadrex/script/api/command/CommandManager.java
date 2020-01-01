package org.quadrex.script.api.command;

import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.QScript;

import java.util.Optional;

public interface CommandManager {

    static CommandManager get() {
        return ScriptPlugin.getInstance().getCommandManager();
    }

    void registerCommand(String name, QCommand executor);

    void unregisterCommand(QCommand command);

    void unregisterCommand(String name);

    void unregisterAll(QScript script);

    Optional<QCommand> getCommand(String label);

}
