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
            //If the task already exists, we must be reloading
            //So we cancel the task and remove it from the task list so that a new task can be generated
            task.cancel();
            plugin.tasks.removeTask(task);
        }
        long delayTicks = (long) (delay * 60 * 20);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, getRunnable(), delayTicks, delayTicks);
        plugin.tasks.addTask(task);
    }

    private Runnable getRunnable() {
        return () -> {
            //If there are no players online, and we should wait for players, simply return
            //This is so that the next announcement successfully made isn't stacked
            if (Bukkit.getOnlinePlayers().isEmpty() && plugin.getConfig().getBoolean("WaitForPlayers")) {
                return;
            }
            //If we have no messages to send, return
            //This might happen if the config reloads unsuccessfully
            if (messages.isEmpty())
                return;
            //Fetch either a random message or the next message in the order
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
            //Process and send out the message
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
