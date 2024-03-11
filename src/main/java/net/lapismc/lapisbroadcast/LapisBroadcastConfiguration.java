package net.lapismc.lapisbroadcast;

import net.lapismc.lapiscore.LapisCoreConfiguration;

import java.util.ArrayList;

public class LapisBroadcastConfiguration extends LapisCoreConfiguration {

    private final LapisBroadcast plugin;

    public LapisBroadcastConfiguration(LapisBroadcast plugin, int configVersion, int messagesVersion) {
        super(plugin, configVersion, messagesVersion, new ArrayList<>());
        this.plugin = plugin;
    }

    @Override
    public void reloadMessages() {
        super.reloadMessages();
        //Check that plugin has finished enabling as this method does get called during startup
        if (plugin != null)
            plugin.service.reloadService();
    }
}
