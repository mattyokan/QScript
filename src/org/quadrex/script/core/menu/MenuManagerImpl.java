package org.quadrex.script.core.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.chat.StringUtil;
import org.quadrex.script.api.menu.Menu;
import org.quadrex.script.api.menu.MenuManager;
import org.quadrex.script.QScript;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MenuManagerImpl implements MenuManager, Listener {

    private ScriptPlugin plugin;
    private Map<Menu, QScript> menus;

    public MenuManagerImpl(ScriptPlugin plugin) {
        this.plugin = plugin;
        menus = new ConcurrentHashMap<>();
    }

    @Override
    public void registerMenu(Menu menu, QScript script) {
        menus.put(menu, script);
    }

    @Override
    public void unregisterMenu(Menu menu) {
        menus.remove(menu);
    }

    @Override
    public void unregisterAll(QScript script) {
        menus.entrySet().stream().filter(e -> e.getValue().equals(script)).map(Map.Entry::getKey).forEach(menus::remove);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Optional<Menu> found = getByInventory(inventory);
        if(found.isPresent()) {
            event.setCancelled(true);
            Menu menu = found.get();
            Player player = (Player) event.getWhoClicked();
            menu.getMenuItem(event.getSlot()).ifPresent(item -> {
                if(item.getAction() != null)
                item.getAction().onClick(player, event.getClick(), item, menu);
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Optional<Menu> found = getByInventory(inventory);
        if(found.isPresent()) {
            Menu menu = found.get();
            Player player = (Player) event.getPlayer();
            Bukkit.getScheduler().runTaskLater(plugin, () -> menu.onClose(player), 1);
        }
    }

    private Optional<Menu> getByInventory(Inventory inventory) {
        return menus.keySet().stream().filter(menu -> StringUtil.color(menu.getTitle()).equalsIgnoreCase(inventory.getTitle()) && inventory.getSize() == menu.getSize()).findAny();
    }

}
