package net.lapismc.lapisbroadcast.commands;

import net.lapismc.lapisbroadcast.LapisBroadcast;
import net.lapismc.lapisbroadcast.commands.tabcomplete.BroadcastMessageOption;
import net.lapismc.lapisbroadcast.commands.tabcomplete.ReloadOption;
import net.lapismc.lapiscore.commands.LapisCoreCommand;
import net.lapismc.lapiscore.commands.tabcomplete.LapisCoreTabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LapisBroadcastCommand extends LapisCoreCommand {

    private final LapisBroadcast plugin;

    public LapisBroadcastCommand(LapisBroadcast core) {
        super(core, "lapisbroadcast", "Broadcast a message", new ArrayList<>(Collections.singletonList("lb")));
        this.plugin = core;
        LapisCoreTabCompleter tabCompleter = new LapisCoreTabCompleter();
        tabCompleter.registerTopLevelOptions(this,
                new ArrayList<>(Arrays.asList(new BroadcastMessageOption(), new ReloadOption())));
        registerTabCompleter(tabCompleter);
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("lapisbroadcast.use")) {
                sendMessage(sender, "Error.NotPermitted");
                return;
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.service.reloadService();
            sendMessage(sender, "Reloaded");
        } else if (args.length > 0) {
            String message = getMessage(new ArrayList<>(Arrays.asList(args)));
            plugin.service.sendMessage(message);
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
