package org.quadrex.script.core.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.placeholder.PlaceholderHook;

public class PlaceholderHookImpl implements PlaceholderHook {

    private ScriptPlugin plugin;
    private PlaceholderHook papiHook;

    public PlaceholderHookImpl(ScriptPlugin plugin) {
        this.plugin = plugin;
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiHook = new PapiHook();
        }
    }

    @Override
    public String setPlaceholders(OfflinePlayer player, String message) {
        return papiHook == null ? message : papiHook.setPlaceholders(player, message);
    }

    @Override
    public String setPlaceholders(Player player, String message) {
        return papiHook == null ? message : papiHook.setPlaceholders(player, message);
    }
}
