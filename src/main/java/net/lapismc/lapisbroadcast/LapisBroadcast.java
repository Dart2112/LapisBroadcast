package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.utils.LapisBroadcastFileWatcher;
import net.lapismc.lapisbroadcast.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LapisBroadcast extends JavaPlugin {

    public BroadcastService service;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        service = new BroadcastService(this);
        new Metrics(this);
        Bukkit.getScheduler().runTaskAsynchronously(this, new LapisBroadcastFileWatcher(this));
        getLogger().info(getDescription().getName() + " v." + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void onDisable() {
        service.stopService();
        getLogger().info(getDescription().getName() + " has been disabled");
    }
}
