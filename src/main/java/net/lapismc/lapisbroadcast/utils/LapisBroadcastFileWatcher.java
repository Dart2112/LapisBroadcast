package net.lapismc.lapisbroadcast.utils;

import net.lapismc.lapisbroadcast.LapisBroadcast;
import net.lapismc.lapiscore.LapisCoreFileWatcher;

import java.io.File;

public class LapisBroadcastFileWatcher extends LapisCoreFileWatcher {

    private LapisBroadcast plugin;

    public LapisBroadcastFileWatcher(LapisBroadcast core) {
        super(core);
        this.plugin = core;
    }

    @Override
    public void fileUpdate(File f) {
        if (f.getName().contains("messages")) {
            plugin.service.reloadService();
        }
    }
}
