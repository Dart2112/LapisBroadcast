package net.lapismc.lapisbroadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPIHook {

    String format(String s, OfflinePlayer p){
        return PlaceholderAPI.setPlaceholders(p, s);
    }

}
