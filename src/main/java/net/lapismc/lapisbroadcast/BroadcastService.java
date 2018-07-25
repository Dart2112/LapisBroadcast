package net.lapismc.lapisbroadcast;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BroadcastService {

    private String prefix;
    private Integer taskId;
    private LapisBroadcast plugin;
    private Integer messageIndex = 0;
    private List<String> messages = new ArrayList<>();
    private Random random = new Random(System.currentTimeMillis());

    BroadcastService(LapisBroadcast plugin) {
        this.plugin = plugin;
        loadMessages();
        startRunnable();
    }

    void reloadService() {
        loadMessages();
        startRunnable();
    }

    void stopService() {
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void loadMessages() {
        prefix = format(plugin.getConfig().getString("Prefix"));
        messages = plugin.getConfig().getStringList("Messages");
    }

    private void startRunnable() {
        Long delay = plugin.getConfig().getLong("Delay") * 20 * 60;
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, getTask(), delay, delay);
    }

    private Runnable getTask() {
        return () -> {
            String message;
            if (plugin.getConfig().getBoolean("RandomOrder")) {
                message = messages.get(random.nextInt(messages.size()));
            } else {
                if (messageIndex >= messages.size()) {
                    messageIndex = 0;
                }
                message = messages.get(messageIndex);
                messageIndex++;
            }
            message = format(message);
            String broadcast = prefix + message;
            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                if (op.isOnline()) {
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        broadcast = new PlaceholderAPIHook().format(broadcast, op);
                    }
                    op.getPlayer().sendMessage(broadcast);
                }
            }
            if (plugin.getConfig().getBoolean("ConsoleLog")) {
                plugin.getLogger().info(broadcast);
            }
        };
    }

    private String format(String s) {
        String primaryColor = plugin.getConfig().getString("PrimaryColor", ChatColor.BLUE.toString());
        String secondaryColor = plugin.getConfig().getString("SecondaryColor", ChatColor.GOLD.toString());
        String message = s.replace("&p", primaryColor).replace("&s", secondaryColor);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
