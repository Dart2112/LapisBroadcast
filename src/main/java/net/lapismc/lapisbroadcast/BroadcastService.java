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
    private boolean waitingForPlayers = false;
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
            while (Bukkit.getOnlinePlayers().size() == 0 && plugin.getConfig().getBoolean("WaitForPlayers")) {
                if (waitingForPlayers) {
                    return;
                }
                waitingForPlayers = true;
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException ignored) {
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
                String consoleMessage = broadcast;
                if (plugin.getConfig().getBoolean("StripColor")) {
                    consoleMessage = ChatColor.stripColor(broadcast);
                }
                Bukkit.getLogger().info(consoleMessage);
            }
            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                if (op.isOnline()) {
                    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                        broadcast = new PlaceholderAPIHook().format(broadcast, op);
                    }
                    op.getPlayer().sendMessage(broadcast);
                }
            }
            waitingForPlayers = false;
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
        prefix = plugin.config.getMessage("Prefix");
        messages = plugin.config.getMessages().getStringList("Messages");
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
        String primaryColor = plugin.config.getMessage("PrimaryColor");
        String secondaryColor = plugin.config.getMessage("SecondaryColor");
        String message = s.replace("&p", primaryColor).replace("&s", secondaryColor);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
