package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.commands.LapisBroadcastCommand;
import net.lapismc.lapiscore.LapisCoreConfiguration;
import net.lapismc.lapiscore.LapisCorePlugin;
import net.lapismc.lapiscore.utils.LapisCoreFileWatcher;
import net.lapismc.lapiscore.utils.Metrics;

public final class LapisBroadcast extends LapisCorePlugin {

    public BroadcastService service;
    public LapisCoreFileWatcher fileWatcher;

    @Override
    public void onEnable() {
        registerConfiguration(new LapisCoreConfiguration(this, 1, 1));
        service = new BroadcastService(this);
        new LapisBroadcastCommand(this);
        new Metrics(this);
        fileWatcher = new LapisCoreFileWatcher(this);
        getLogger().info(getDescription().getName() + " v." + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void onDisable() {
        service.stopService();
        fileWatcher.stop();
        getLogger().info(getDescription().getName() + " has been disabled");
    }
}
