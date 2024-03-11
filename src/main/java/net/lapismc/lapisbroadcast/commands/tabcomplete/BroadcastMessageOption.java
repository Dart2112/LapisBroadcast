package net.lapismc.lapisbroadcast.commands.tabcomplete;

import net.lapismc.lapiscore.commands.tabcomplete.LapisTabOption;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BroadcastMessageOption implements LapisTabOption {
    @Override
    public List<String> getOptions(CommandSender sender) {
        return new ArrayList<>(Collections.singleton("(Message to broadcast)"));
    }

    @Override
    public List<LapisTabOption> getChildren(CommandSender sender) {
        return null;
    }
}
