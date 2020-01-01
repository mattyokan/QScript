package org.quadrex.script.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.quadrex.script.api.chat.StringUtil;
import org.quadrex.script.QScript;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public abstract class Menu {

    private QScript script;
    public Map<Integer, MenuItem> menuItems;

    public Menu(QScript script) {
        menuItems = new ConcurrentHashMap<>();
        this.script = script;
        loadItems();
        MenuManager.get().registerMenu(this, script);
    }

    public void loadItems() {
        menuItems = new ConcurrentHashMap<>();
    }

    public Inventory getInventory(Player player) {
         Inventory inventory = Bukkit.getServer().createInventory(null, getSize(), StringUtil.color(getTitle()));
         menuItems.forEach((slot, item) -> {
             ItemBuilder builder = item.getItem().clone();
             ItemStack stack = setPlaceholders(player, builder, item);
             inventory.setItem(slot, stack);
         });
         setBorders(inventory);
         return inventory;
    }

    public void open(Player player) {
        player.openInventory(getInventory(player));
        onOpen(player);
    }

    public void setBorders(Inventory inventory) {
        IntStream.range(0, inventory.getSize()).filter(slot -> inventory.getItem(slot) == null).forEach(slot -> inventory.setItem(slot, getBorderItem()));
    }

    /**
     * Set placeholders for an item. Override this method in your custom menu to set non PlaceholderAPI placeholders.
     * @param player The player to set placeholders for.
     * @param builder The ItemBuilder to set.
     * @param item the MenuItem to set with.
     * @return The item, set with placeholders.
     */
    public ItemStack setPlaceholders(Player player, ItemBuilder builder, MenuItem item) {
        return builder.setPlaceholderAPI(player).toItemStack();
    }

    public Optional<MenuItem> getMenuItem(int slot) {
        return Optional.ofNullable(menuItems.get(slot));
    }

    /**
     * Method that is executed when a menu is opened.
     */
    public void onOpen(Player player) {

    }

    /**
     * Method that is executed exactly one tick after a menu is closed.
     */
    public void onClose(Player player) {

    }

    public abstract String getTitle();

    public abstract int getSize();

    public abstract ItemStack getBorderItem();

    public QScript getScript() {
        return script;
    }
}
