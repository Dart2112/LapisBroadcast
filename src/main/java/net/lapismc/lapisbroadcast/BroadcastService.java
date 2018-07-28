package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.utils.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BroadcastService {

    private String prefix;
    private ScheduledFuture task;
    private LapisBroadcast plugin;
    private Integer messageIndex = 0;
    private List<String> messages = new ArrayList<>();
    private Random random = new Random(System.currentTimeMillis());

    BroadcastService(LapisBroadcast plugin) {
        this.plugin = plugin;
        loadMessages();
        startRunnable();
    }

    private Runnable getRunnable() {
        return () -> {
            if (plugin.getConfig().getBoolean("WaitForPlayers")) {
                while (Bukkit.getOnlinePlayers().size() == 0) {
                    try {
                        Thread.sleep(1000 * 2);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            if (messages.size() == 0)
                return;
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
            message = colorize(message);
            String broadcast = prefix + message;
            if (plugin.getConfig().getBoolean("ConsoleLog")) {
                Bukkit.getLogger().info(broadcast);
            }
            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                if (op.isOnline()) {
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        broadcast = new PlaceholderAPIHook().format(broadcast, op);
                    }
                    op.getPlayer().sendMessage(broadcast);
                }
            }
        };
    }

    public void reloadService() {
        loadMessages();
        startRunnable();
    }

    void stopService() {
        if (task != null) {
            task.cancel(false);
        }
    }

    private void loadMessages() {
        prefix = colorize(plugin.getConfig().getString("Prefix"));
        messages = plugin.getConfig().getStringList("Messages");
    }

    private void startRunnable() {
        Double delay = plugin.getConfig().getDouble("Delay", 2) * 60;
        if (task != null) {
            task.cancel(false);
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        task = scheduler.scheduleWithFixedDelay(getRunnable(), 0L, delay.longValue(), TimeUnit.SECONDS);
    }

    private String colorize(String s) {
        String primaryColor = plugin.getConfig().getString("PrimaryColor", ChatColor.BLUE.toString());
        String secondaryColor = plugin.getConfig().getString("SecondaryColor", ChatColor.GOLD.toString());
        String message = s.replace("&p", primaryColor).replace("&s", secondaryColor);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
