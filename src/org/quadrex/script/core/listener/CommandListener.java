package org.quadrex.script.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.quadrex.script.api.command.CommandManager;
import org.quadrex.script.api.command.QCommand;
import org.quadrex.script.api.chat.StringUtil;

import java.util.Arrays;
import java.util.Optional;

public class CommandListener implements Listener {

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String[] parts = message.split(" ");
        if(parts.length >= 1) {
            String label = parts[0].replaceFirst("/", "");
            Optional<QCommand> command = getCommand(label);
            if(command.isPresent()) {
                QCommand present = command.get();
                String[] args = new String[0];
                if (parts.length > 1) {
                    args = Arrays.copyOfRange(parts, 1, parts.length);
                }
                try {
                    present.execute(event.getPlayer(), label, args);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    event.getPlayer().sendMessage(StringUtil.color("&c&l[!]&c An internal error occurred."));
                }
                event.setCancelled(true);
            }
        }
    }

    private Optional<QCommand> getCommand(String label) {
        CommandManager manager = CommandManager.get();
        return manager.getCommand(label);
    }
}
