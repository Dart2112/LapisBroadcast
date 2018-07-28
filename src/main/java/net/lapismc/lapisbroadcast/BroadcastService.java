package net.lapismc.lapisbroadcast;

import net.lapismc.lapisbroadcast.utils.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BroadcastService {

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

    public void reloadService() {
        loadMessages();
        startRunnable();
    }

    void stopService() {
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void loadMessages() {
        prefix = colorize(plugin.getConfig().getString("Prefix"));
        messages = plugin.getConfig().getStringList("Messages");
    }

    private void startRunnable() {
        Double delay = plugin.getConfig().getDouble("Delay") * 20 * 60;
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, getTask(), delay.longValue(), delay.longValue()).getTaskId();
    }

    private Runnable getTask() {
        return () -> {
            waitForPlayers(Thread.currentThread());
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

    private void waitForPlayers(Thread thread) {
        if (plugin.getConfig().getBoolean("WaitForPlayers")) {
            while (Bukkit.getOnlinePlayers().size() == 0) {
                try {
                    thread.wait(1000 * 5);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private String colorize(String s) {
        String primaryColor = plugin.getConfig().getString("PrimaryColor", ChatColor.BLUE.toString());
        String secondaryColor = plugin.getConfig().getString("SecondaryColor", ChatColor.GOLD.toString());
        String message = s.replace("&p", primaryColor).replace("&s", secondaryColor);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
