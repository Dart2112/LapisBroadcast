package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.commands.LapisBroadcastCommand;
import net.lapismc.lapisbroadcast.utils.LapisBroadcastFileWatcher;
import net.lapismc.lapisbroadcast.utils.Metrics;
import net.lapismc.lapiscore.LapisCoreConfiguration;
import net.lapismc.lapiscore.LapisCorePlugin;

public final class LapisBroadcast extends LapisCorePlugin {

    public BroadcastService service;

    @Override
    public void onEnable() {
        registerConfiguration(new LapisCoreConfiguration(this, 1, 1));
        service = new BroadcastService(this);
        new LapisBroadcastCommand(this);
        new Metrics(this);
        new LapisBroadcastFileWatcher(this);
        getLogger().info(getDescription().getName() + " v." + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void onDisable() {
        service.stopService();
        getLogger().info(getDescription().getName() + " has been disabled");
    }
}
