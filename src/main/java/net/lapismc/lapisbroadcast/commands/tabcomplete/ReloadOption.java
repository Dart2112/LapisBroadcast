package net.lapismc.lapisbroadcast.commands.tabcomplete;

import net.lapismc.lapiscore.commands.tabcomplete.LapisTabOption;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadOption implements LapisTabOption {

    @Override
    public List<String> getOptions(CommandSender sender) {
        return Collections.singletonList("reload");
    }

    @Override
    public List<LapisTabOption> getChildren(CommandSender sender) {
        return null;
    }
}
