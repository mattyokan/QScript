package org.quadrex.script.api.menu;

public class MenuItem {

    private int slot;
    private String name;
    private MenuAction action;
    private ItemBuilder item;

    public MenuItem(String name, int slot) {
            this.name = name;
            this.slot = slot;
    }

    public MenuItem(int slot) {
        this(null, slot);
    }

    public MenuItem setAction(MenuAction action) {
        this.action = action;
        return this;
    }

    public MenuItem setItem(ItemBuilder item) {
        this.item = item;
        return this;
    }

    public MenuAction getAction() {
        return action;
    }

    public ItemBuilder getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
