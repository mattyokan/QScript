package org.quadrex.script.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@FunctionalInterface
public interface MenuAction {

    void onClick(Player player, ClickType clickType, MenuItem item, Menu menu);

}
