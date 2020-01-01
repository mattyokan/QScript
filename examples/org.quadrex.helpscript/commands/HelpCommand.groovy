package org.quadrex.helpscript.commands

import org.bukkit.command.CommandSender
import org.quadrex.script.api.command.QCommand
import org.quadrex.script.QScript
import org.quadrex.script.api.chat.StringUtil

import java.util.stream.Stream

class HelpCommand extends QCommand {

    HelpCommand(QScript script) {
        super(script, "help", Arrays.asList("qhelp", "helps", "helpsystem"))
    }

    @Override
    void execute(CommandSender sender, String commandLabel, String[] args) {
        Stream.of(
            "",
            "&b&lQScript Help",
            "",
            "&f/help",
            "&7Reads this command"
        ).map({line -> StringUtil.color(line)})
        .forEach({line -> sender.sendMessage(line)})
    }
}