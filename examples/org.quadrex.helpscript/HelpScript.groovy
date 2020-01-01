package org.quadrex.helpscript

import org.quadrex.helpscript.commands.HelpCommand
import org.quadrex.script.api.command.CommandManager
import org.quadrex.script.QScript;

class HelpScript extends QScript {

    @Override
    void onLoad() {
        CommandManager.get().registerCommand("help", new HelpCommand())
    }

}