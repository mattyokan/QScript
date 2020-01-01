package org.quadrex.script;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.quadrex.script.api.command.QCommand;
import org.quadrex.script.api.listener.ListenerManager;
import org.quadrex.script.api.menu.MenuManager;
import org.quadrex.script.api.placeholder.PlaceholderHook;
import org.quadrex.script.api.scheduler.QScheduler;
import org.quadrex.script.core.command.QScriptCommand;
import org.quadrex.script.api.command.CommandManager;
import org.quadrex.script.core.listener.CommandListener;
import org.quadrex.script.core.menu.MenuManagerImpl;
import org.quadrex.script.core.model.*;
import org.quadrex.script.api.script.ScriptManager;
import org.quadrex.script.core.placeholder.PlaceholderHookImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptPlugin extends JavaPlugin {

    private static ScriptPlugin instance;

    private ScriptManager scriptManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private MenuManager menuManager;
    private QScheduler qScheduler;

    private PlaceholderHook placeholderHook;

    private QCommand qScriptCommand;

    private QScript dummyScript;

    private File rootDirectory;

    @Override
    public void onEnable() {
        instance = this;
        dummyScript = new DummyScript();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CommandListener(), this);
        loadFolder();
        loadManagers();
        loadCommands();
        pm.registerEvents(menuManager, this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SchedulerPurgeTask(), 20, 20);
    }

    private void loadCommands() {
        qScriptCommand = new QScriptCommand(dummyScript);
        commandManager.registerCommand("qscript", qScriptCommand);
    }

    private void loadManagers() {
        scriptManager = new ScriptManagerImpl(this);
        commandManager = new PreprocessCommandManager(this);
        listenerManager = new ListenerManagerImpl(this);
        menuManager = new MenuManagerImpl(this);
        qScheduler = new QSchedulerImpl(this);

        placeholderHook = new PlaceholderHookImpl(this);
    }

    @Override
    public void onDisable() {
        scriptManager.unloadAll();
        scriptManager.onDisable();
        commandManager.unregisterCommand(qScriptCommand);
    }

    private void loadFolder() {
        try {
            rootDirectory = new File(new File(getDataFolder().getAbsoluteFile().getPath() + "/../../scripts/").getCanonicalPath());
            Files.createDirectories(Paths.get(rootDirectory.getAbsolutePath()));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public PlaceholderHook getPlaceholderHook() {
        return placeholderHook;
    }

    public QScheduler getQScheduler() {
        return qScheduler;
    }

    public static ScriptPlugin getInstance() {
        return instance;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }
}
