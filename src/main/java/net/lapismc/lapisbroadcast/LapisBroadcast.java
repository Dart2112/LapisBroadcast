package net.lapismc.lapisbroadcast;

import org.bukkit.plugin.java.JavaPlugin;

public final class LapisBroadcast extends JavaPlugin {

    BroadcastService service;

    @Override
    public void onEnable() {
        service = new BroadcastService(this);
        new LapisBroadcastFileWatcher(this);
    }

    @Override
    public void onDisable() {
        service.stopService();
    }
}
