package org.quadrex.script.api.listener;

import org.bukkit.event.Listener;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.QScript;

public interface ListenerManager {

    static ListenerManager get() {
        return ScriptPlugin.getInstance().getListenerManager();
    }

    void registerListener(QScript script, Listener listener);

    void unregisterListener(Listener listener);

    void unregisterAll(QScript script);
}
