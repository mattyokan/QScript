package org.quadrex.script.api.menu;

import org.bukkit.event.Listener;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.QScript;

public interface MenuManager extends Listener {

    static MenuManager get() {
        return ScriptPlugin.getInstance().getMenuManager();
    }

    void registerMenu(Menu menu, QScript script);

    void unregisterMenu(Menu menu);

    void unregisterAll(QScript script);
}
