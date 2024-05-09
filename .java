package loosa.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Randomitemplugin extends JavaPlugin implements Listener {

    private Map<Player, Long> lastItemTime = new HashMap<>();
    private Random random = new Random();
    private boolean timerRunning = false;
    private int countdown = 10;
    private BossBar bossBar;
    //initialize
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("run").setExecutor(this);
        getCommand("stop").setExecutor(this);
        bossBar = Bukkit.createBossBar("Time left: 10 seconds", BarColor.GREEN, BarStyle.SEGMENTED_10);
    }
    //running
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("run")) {
            if (!timerRunning) {
                startTimer();
                timerRunning = true;
                bossBar.setVisible(true);
                Bukkit.broadcastMessage("§aThe plugin has started");
            } else {
                sender.sendMessage("§eThe plugin is already running.");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("stop")) {
            if (timerRunning) {
                Bukkit.getScheduler().cancelTasks(this);
                timerRunning = false;
                countdown = 10;
                bossBar.setVisible(false);
                Bukkit.broadcastMessage("§4The plugin has stopped");
            } else {
                sender.sendMessage("§eThe plugin is already stopped.");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("dev")) {
            sender.sendMessage("§dThis plugin was developed by LoosaMagee!");
            return true;
        } else if (command.getName().equalsIgnoreCase("info")) {
            sender.sendMessage("§b-This plugin is my first ever and i'm very happy with how it turned out! I am going to continue to learn and make more plugins in the future. I hope we meet again! -Love Loosa <3");
            return true;
        } else if (command.getName().equalsIgnoreCase("help")) {
            sender.sendMessage("§b======== §aPlugin Help §b========");
            sender.sendMessage("§a- §e/run §7= Start plugin");
            sender.sendMessage("§a- §e/stop §7= Kill plugin");
            sender.sendMessage("§a- §e/dev §7= Who made this Plugin?");
            sender.sendMessage("§a- §e/info §7= Why does this project exist?");
            sender.sendMessage("§b==========================");
            return true;
        }
        return false;
    }
    //function
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        lastItemTime.put(player, System.currentTimeMillis());
        player = event.getPlayer();
        player.sendTitle("§aWelcome", "§fDo §6/run §fto start the Random Item Challenge!", 30, 80, 20);
    }

    private void startTimer() {
        bossBar.setProgress(1.0);
        bossBar.addPlayer(Bukkit.getOnlinePlayers().iterator().next());
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (countdown == 1) {
                    lastItemTime.put(player, System.currentTimeMillis());
                    ItemStack item = getRandomItem();
                    player.getInventory().addItem(item);
                    countdown = 10;
                } else {
                    countdown--;
                }
                bossBar.setTitle("Time left: " + countdown + " seconds");
                bossBar.setProgress((double) countdown / 10);
            }
        }, 0, 20); // 1 second
    }

    private ItemStack getRandomItem() {
        Material[] materials = Material.values();
        return new ItemStack(materials[random.nextInt(materials.length)]);
    }
}
