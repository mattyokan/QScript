package org.quadrex.script.core.command;

import org.bukkit.command.CommandSender;
import org.quadrex.script.ScriptPlugin;
import org.quadrex.script.api.command.QCommand;
import org.quadrex.script.QScript;
import org.quadrex.script.api.script.ScriptManager;
import org.quadrex.script.core.model.ScriptActionResult;
import org.quadrex.script.api.chat.StringUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class QScriptCommand extends QCommand {

    public QScriptCommand(QScript dummy) {
        super(dummy, "qscript", Arrays.asList("qscripts", "quadrexscript", "quadrexscripts"));
    }

    @Override
    public void execute(CommandSender sender, String commandLabel, String[] args) {
        ScriptPlugin plugin = ScriptPlugin.getInstance();
        ScriptManager scriptManager = plugin.getScriptManager();
        switch (args.length) {
            case 0:
                sendHelp(sender);
                break;
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelp(sender);
                } else if (args[0].equalsIgnoreCase("unloadall")) {
                    scriptManager.unloadAll();
                } else if (args[0].equalsIgnoreCase("refresh")) {
                    scriptManager.updateAttributeIndices();
                    sender.sendMessage(StringUtil.color("&8[&3&lQ&b&lScript&8]&7 Updated attribute indices!"));
                }
                break;
            case 2:
                String scriptName = args[1];
                switch (args[0].toLowerCase()) {
                    case "reload":
                        handleAction(sender, scriptName, scriptManager.reloadScript(scriptName), "reload");
                    case "load":
                        handleAction(sender, scriptName, scriptManager.loadScript(scriptName), "load");
                        break;
                    case "unload":
                        handleAction(sender, scriptName, scriptManager.unloadScript(scriptName), "unload");
                        break;
                }
                break;
            default:
                sendHelp(sender);
        }
    }

    private void handleAction(CommandSender sender, String script, ScriptActionResult result, String actionType) {
        switch (result) {

            case SUCCESS:
                String message = getMessage(actionType);
                message = message.replace("%name%", script);
                sender.sendMessage(StringUtil.color(message));
                break;
            case ALREADY_EXISTS:
                sender.sendMessage(StringUtil.color("&8[&3&lQ&b&lScript&8]&7 That action already exists! (plugin is already enabled or loaded)"));
                break;
            case ERROR:
                sender.sendMessage(StringUtil.color("&8[&3&lQ&b&lScript&8]&7 An error occurred while attempting to perform that action (check logs)"));
                break;
            case DOES_NOT_EXIST:
                sender.sendMessage(StringUtil.color("&8[&3&lQ&b&lScript&8]&7 That script doesn't exist (couldn't find a script for " + script + ", try updating indices)."));
                break;
        }
    }

    private String getMessage(String actionType) {
        switch (actionType.toLowerCase()) {
            case "reload":
                return "&8[&3&lQ&b&lScript&8]&7 &f%name%&7 has been &breloaded&7.";
            case "load":
                return "&8[&3&lQ&b&lScript&8]&7 &f%name%&7 has been &aloaded&7.";
            case "unload":
                return "&8[&3&lQ&b&lScript&8]&7 &f%name%&7 has been &cunloaded&7.";
            default:
                return "&8[&3&lQ&b&lScript&8]&7 Action performed successfully.";
        }
    }

    private void sendHelp(CommandSender sender) {
        Stream.of(
                "",
                "&3&lQ&b&lScript Help",
                "&7(You are on version 1.1.1-ALPHA)",
                "",
                "&f/qscript refresh",
                "&7Refresh attribute indices to look for new scripts.",
                "",
                "&f/qscript load <name>",
                "&7Loads an (unloaded) script.",
                "",
                "&f/qscript unload <name>",
                "&7Unloads a loaded script.",
                "",
                "&f/qscript unloadall",
                "&7We call this the nuclear option.",
                "&7(only use this so you can unload and reload this plugin.)",
                ""
        ).map(StringUtil::color).forEach(sender::sendMessage);
    }
}
