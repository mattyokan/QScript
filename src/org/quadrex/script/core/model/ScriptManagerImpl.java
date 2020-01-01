package org.quadrex.script.core.model;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovySystem;
import org.bukkit.Bukkit;
import org.quadrex.script.QScript;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.command.CommandManager;
import org.quadrex.script.api.listener.ListenerManager;
import org.quadrex.script.api.logger.QLog;
import org.quadrex.script.api.menu.MenuManager;
import org.quadrex.script.api.scheduler.QScheduler;
import org.quadrex.script.api.script.ScriptManager;
import org.quadrex.script.core.loader.ScriptLoader;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ScriptManagerImpl implements ScriptManager {

    private ScriptPlugin plugin;
    private ScriptLoader scriptLoader;

    private Map<String, ScriptAttributes> scriptNames;
    private Map<ScriptAttributes, QScript> loadedScripts;

    private Map<ScriptAttributes, GroovyClassLoader> classLoaders;

    public ScriptManagerImpl(ScriptPlugin plugin) {
        this.plugin = plugin;
        loadClassLoader();
        scriptLoader = new ScriptLoader();

        scriptNames = new ConcurrentHashMap<>();
        loadedScripts = new ConcurrentHashMap<>();
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> load(true), 1);
    }

    public static File getRootDirectory(File pluginDataFolder) {
        return ScriptPlugin.getInstance().getRootDirectory();
    }

    public void onDisable() {
        classLoaders.forEach((attributes, loader) -> deleteClassLoader(attributes));
    }

    private void loadClassLoader() {
        classLoaders = new ConcurrentHashMap<>();
        /*classLoader = new GroovyClassLoader(plugin.getClass().getClassLoader());
        classLoader.addClasspath(ScriptPlugin.getInstance().getRootDirectory().getAbsolutePath());*/
    }

    private GroovyClassLoader createClassLoader(ScriptAttributes attributes) {
        GroovyClassLoader classLoader = new GroovyClassLoader(plugin.getClass().getClassLoader());
        classLoader.addClasspath(ScriptPlugin.getInstance().getRootDirectory().getAbsolutePath());
        classLoaders.put(attributes, classLoader);
        return classLoader;
    }

    private void deleteClassLoader(ScriptAttributes attributes) {
        GroovyClassLoader classLoader = classLoaders.get(attributes);
        if (classLoader != null) {
            Arrays.stream(classLoader.getLoadedClasses()).forEach(clazz -> GroovySystem.getMetaClassRegistry().removeMetaClass(clazz));
            classLoader.clearCache();
            try {
                classLoader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.gc();
        }
    }

    private void load(boolean loadScripts) {

        scriptLoader.getAllScriptFiles(ScriptPlugin.getInstance().getRootDirectory()).forEach(scriptFile -> {
            QLog.log(() -> "Loading attributes for the script file with the path " + scriptFile.getAbsolutePath());
            Optional<ScriptAttributes> foundAttributes = ScriptAttributes.fromJsonFile(scriptFile);
            if (foundAttributes.isPresent()) {
                ScriptAttributes attributes = foundAttributes.get();
                if (scriptNames.containsKey(attributes.getName())) {
                    ScriptAttributes current = scriptNames.get(attributes.getName());
                    if (current.getFile().equals(attributes.getFile())) {
                        QLog.log(Level.FINE, () ->
                                "Script " + current.getName() + "is loaded, so its attributes will not be updated.\n" +
                                        "You must unload the script if you wish to update its version, main class, or dependencies."
                        );
                    } else {
                        QLog.log(Level.WARNING, () ->
                                "====[ QScript Loading Error! ]====\n" +
                                        "Encountered a duplicate script (" + scriptFile.getAbsolutePath() + ")!\n" +
                                        "Currently Loaded:" + current.getFile().getPath() + "\n" +
                                        "Name:" + current.getName() + "\n" +
                                        "Main QScript:" + current.getMain() + "\n" +
                                        "Version:" + current.getVersion() + "\n" +
                                        "=================================="
                        );
                    }
                } else {
                    QLog.log(() -> "Loaded the attributes for the script " + attributes.getName());
                    scriptNames.put(attributes.getName(), attributes);
                    if (loadScripts) {
                        QLog.log(() -> "Loading and enabling " + attributes.getName());
                        boolean result = loadScript(attributes, scriptFile);
                        QLog.log(() -> (result ? "Loaded" : "Failed to load") + " the script " + attributes.getName());
                    }
                }
            }
        });
    }

    private void load(QScript script) {
        script.onLoad();
    }

    private void unload(QScript script) {
        ListenerManager.get().unregisterAll(script);
        CommandManager.get().unregisterAll(script);
        MenuManager.get().unregisterAll(script);
        QScheduler.get().unregisterAll(script);
        script.onUnload();
    }

    private Optional<QScript> instantiateScript(ScriptAttributes attributes, File file) {
        try {
            QLog.log(() -> "Instantiating " + file.getAbsolutePath());
            GroovyClassLoader classLoader = createClassLoader(attributes);
            Class scriptClass = classLoader.parseClass(file);
            Object instantiated = scriptClass.newInstance();
            if (instantiated instanceof QScript) {
                return Optional.of((QScript) instantiated);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public ScriptActionResult loadScript(String scriptName) {
        ScriptAttributes attributes = scriptNames.get(scriptName);
        if (attributes == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        } else if (loadedScripts.containsKey(attributes)) {
            return ScriptActionResult.ALREADY_EXISTS;
        }
        return loadScript(attributes, attributes.getFile()) ? ScriptActionResult.SUCCESS : ScriptActionResult.ERROR;
    }

    private boolean loadScript(ScriptAttributes attributes, File scriptFile) {
        File mainFile = new File(scriptFile.getParentFile().getAbsolutePath() + "/" + attributes.getMain());
        Optional<QScript> loadedScript = instantiateScript(attributes, mainFile);
        if (loadedScript.isPresent()) {
            QScript script = loadedScript.get();
            load(script);
            loadedScripts.put(attributes, script);
            return true;
        }
        return false;
    }

    @Override
    public ScriptActionResult unloadScript(String scriptName) {
        ScriptAttributes attributes = scriptNames.get(scriptName);
        if (attributes == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        } else if (!loadedScripts.containsKey(attributes)) {
            return ScriptActionResult.DOES_NOT_EXIST;
        }
        return unloadScript(attributes) ? ScriptActionResult.SUCCESS : ScriptActionResult.ERROR;
    }

    private boolean unloadScript(ScriptAttributes attributes) {
        Optional<QScript> loadedScript = Optional.ofNullable(loadedScripts.get(attributes));
        if (loadedScript.isPresent()) {
            QScript script = loadedScript.get();
            unload(script);
            loadedScripts.remove(attributes);
            deleteClassLoader(attributes);
            return true;
        }
        return false;
    }

    @Override
    public ScriptActionResult reloadScript(String scriptName) {
        ScriptActionResult result = unloadScript(scriptName);
        if (result == ScriptActionResult.SUCCESS) {
            return loadScript(scriptName);
        }
        return result;
    }

    /*@Override
    public ScriptActionResult enableScript(String scriptName) {
        ScriptAttributes attributes = scriptNames.get(scriptName);
        if (attributes == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        }
        QScript found = loadedScripts.get(attributes);
        if (found == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        }
        if (found.isEnabled()) {
            return ScriptActionResult.ALREADY_EXISTS;
        }
        try {
            found.onEnable();
            found.setEnabled(true);
            return ScriptActionResult.SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ScriptActionResult.ERROR;
        }
    }*/

    /*@Override
    public ScriptActionResult disableScript(String scriptName) {
        ScriptAttributes attributes = scriptNames.get(scriptName);
        if (attributes == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        }
        QScript found = loadedScripts.get(attributes);
        if (found == null) {
            return ScriptActionResult.DOES_NOT_EXIST;
        }
        if (!found.isEnabled()) {
            return ScriptActionResult.ALREADY_EXISTS;
        }
        try {
            found.onDisable();
            found.setEnabled(false);
            return ScriptActionResult.SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ScriptActionResult.ERROR;
        }
    }*/

    @Override
    public void updateAttributeIndices() {
        load(false);
    }

    @Override
    public void unloadAll() {
        loadedScripts.forEach((script, loaded) -> unloadScript(script));
    }
}
