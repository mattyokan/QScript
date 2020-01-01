package org.quadrex.script.api.placeholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.quadrex.script.ScriptPlugin;

public interface PlaceholderHook {

    static PlaceholderHook get() {
        return ScriptPlugin.getInstance().getPlaceholderHook();
    }

    String setPlaceholders(OfflinePlayer player, String message);

    String setPlaceholders(Player player, String message);

}
