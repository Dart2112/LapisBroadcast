package net.lapismc.lapisbroadcast;

import org.bukkit.plugin.java.JavaPlugin;

public final class LapisBroadcast extends JavaPlugin {

    BroadcastService service;

    @Override
    public void onEnable() {
        service = new BroadcastService(this);
        new LapisBroadcastFileWatcher(this);
        getLogger().info(getDescription().getName() + " v." + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void onDisable() {
        service.stopService();
        getLogger().info(getDescription().getName() + " has been disabled");
    }
}
