package net.lapismc.lapisbroadcast;

import net.lapismc.lapiscore.placeholder.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BroadcastService {

    private final LapisBroadcast plugin;
    private final Random random = new Random(System.currentTimeMillis());
    private String prefix;
    private BukkitTask task;
    private Integer messageIndex = 0;
    private List<String> messages = new ArrayList<>();

    BroadcastService(LapisBroadcast plugin) {
        this.plugin = plugin;
        loadMessages();
        startRunnable();
    }

    public void reloadService() {
        loadMessages();
        startRunnable();
    }

    private void loadMessages() {
        prefix = plugin.config.getMessage("Prefix");
        messages = plugin.config.getMessages().getStringList("Messages");
    }

    private void startRunnable() {
        double delay = plugin.getConfig().getDouble("Delay", 2);
        if (task != null) {
            task.cancel();
            plugin.tasks.removeTask(task);
        }
        long delayTicks = (long) (delay * 60 * 20);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, getRunnable(), delayTicks, delayTicks);
        plugin.tasks.addTask(task);
    }

    private Runnable getRunnable() {
        return () -> {
            double delay = plugin.getConfig().getDouble("Delay", 2) * 60;
            while (Bukkit.getOnlinePlayers().isEmpty() && plugin.getConfig().getBoolean("WaitForPlayers")) {
                try {
                    Thread.sleep((long) (1000 * delay));
                } catch (InterruptedException ignored) {
                }
            }
            if (messages.isEmpty())
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
            sendMessage(message);
        };
    }

    public void sendMessage(String message) {
        message = plugin.config.colorMessage(message);
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
                    broadcast = PlaceholderAPIHook.processPlaceholders(op, broadcast);
                }
                Player p = op.getPlayer();
                if (p != null)
                    p.sendMessage(broadcast);
            }
        }
    }

}
