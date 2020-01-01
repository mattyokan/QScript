package org.quadrex.script.core.model;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.listener.ListenerManager;
import org.quadrex.script.QScript;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListenerManagerImpl implements ListenerManager {

    private ScriptPlugin plugin;

    private Map<Listener, QScript> listeners;

    public ListenerManagerImpl(ScriptPlugin plugin) {
        this.plugin = plugin;
        listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void registerListener(QScript script, Listener listener) {
        listeners.put(listener, script);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
        listeners.remove(listener);
    }

    @Override
    public void unregisterAll(QScript script) {
        listeners.entrySet().stream().filter(e -> e.getValue().equals(script)).map(Map.Entry::getKey).forEach(listener -> {
            HandlerList.unregisterAll(listener);
            listeners.remove(listener);
        });

    }
}
