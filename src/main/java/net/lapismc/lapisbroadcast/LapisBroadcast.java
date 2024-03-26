package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.commands.LapisBroadcastCommand;
import net.lapismc.lapiscore.LapisCorePlugin;
import net.lapismc.lapiscore.utils.LapisCoreFileWatcher;
import net.lapismc.lapiscore.utils.Metrics;

public final class LapisBroadcast extends LapisCorePlugin {

    public BroadcastService service;
    public LapisCoreFileWatcher fileWatcher;

    @Override
    public void onEnable() {
        registerConfiguration(new LapisBroadcastConfiguration(this, 1, 2));
        service = new BroadcastService(this);
        new LapisBroadcastCommand(this);
        new Metrics(this, 2945);
        fileWatcher = new LapisCoreFileWatcher(this);
        getLogger().info(getDescription().getName() + " v." + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        //Intercept the reload here, restart the service so that it uses the new delay value
        //Verify that the service isn't null, as this is called during startup
        if (service != null)
            service.reloadService();
    }
}
