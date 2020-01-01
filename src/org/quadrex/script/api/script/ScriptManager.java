package org.quadrex.script.api.script;

import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.core.model.ScriptActionResult;

public interface ScriptManager {

    static ScriptManager get() {
        return ScriptPlugin.getInstance().getScriptManager();
    }

    ScriptActionResult loadScript(String scriptName);

    ScriptActionResult unloadScript(String script);

    ScriptActionResult reloadScript(String scriptName);

    void updateAttributeIndices();

    void unloadAll();

    void onDisable();
}
