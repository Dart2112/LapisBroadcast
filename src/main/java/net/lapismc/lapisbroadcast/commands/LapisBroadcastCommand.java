package net.lapismc.lapisbroadcast.commands;

import net.lapismc.lapisbroadcast.LapisBroadcast;
import net.lapismc.lapiscore.LapisCoreCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LapisBroadcastCommand extends LapisCoreCommand {

    private LapisBroadcast plugin;

    public LapisBroadcastCommand(LapisBroadcast core) {
        super(core, "lapisbroadcast", "Broadcast a message", new ArrayList<>(Collections.singletonList("lb")));
        this.plugin = core;
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("lapisbroadcast.use")) {
                sendMessage(sender, "Error.NotPermitted");
                return;
            }
        }
        if (args.length > 0) {
            String message = ChatColor.translateAlternateColorCodes('&',
                    getMessage(new ArrayList<>(Arrays.asList(args))));
            String toBroadcast = plugin.config.getMessage("Prefix") + message;
            Bukkit.broadcastMessage(toBroadcast);
        } else {
            sendMessage(sender, "Error.NoArguments");
        }
    }

    private String getMessage(ArrayList<String> arguments) {
        StringBuilder message = new StringBuilder();
        for (String s : arguments) {
            message.append(s).append(" ");
        }
        return message.toString();
    }
}
