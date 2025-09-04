package tetyastan.customSteveChaosReworked;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import tetyastan.customSteveChaosReworked.arenas.ArenasConfig;
import tetyastan.customSteveChaosReworked.duels.DuelConfig;
import tetyastan.customSteveChaosReworked.game.GameExecute;
import tetyastan.customSteveChaosReworked.game.GameTimer;
import tetyastan.customSteveChaosReworked.game.ShopExecute;
import tetyastan.customSteveChaosReworked.items.customenchs.Adrenaline;
import tetyastan.customSteveChaosReworked.items.customenchs.CustomEnchantListener;
import tetyastan.customSteveChaosReworked.items.customenchs.CustomEnchantRegistry;
import tetyastan.customSteveChaosReworked.listeners.CancelListener;
import tetyastan.customSteveChaosReworked.listeners.JoinListener;
import tetyastan.customSteveChaosReworked.listeners.PotionListener;
import tetyastan.customSteveChaosReworked.map.Map;
import tetyastan.customSteveChaosReworked.map.MapConfig;
import tetyastan.customSteveChaosReworked.map.MapExecute;
import tetyastan.customSteveChaosReworked.players.LastMenuExecute;
import tetyastan.customSteveChaosReworked.utils.BossBar;
import tetyastan.customSteveChaosReworked.utils.Config;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;


    private GameTimer timer;
    @Setter
    private int minPlayers = 2;
    private MapConfig mapConfig;
    private ArenasConfig arenasConfig;
    private DuelConfig duelConfig;
    private Config language;

    @Override
    public void onEnable() {

        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("Error creating plugin folder! " + getDataFolder().getAbsolutePath());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        language = new Config(new File(getDataFolder(), "language.yml"), true);
        mapConfig = new MapConfig(new File(getDataFolder(), "map.yml"));
        arenasConfig = new ArenasConfig(new File(getDataFolder(), "arenas.yml"));
        duelConfig = new DuelConfig(new File(getDataFolder(), "duels.yml"));

        Bukkit.getScheduler().runTask(this, () -> {
            try {
                Map.loadMap(mapConfig, arenasConfig);
                getLogger().info("Map loaded successfully.");
            } catch (Exception e) {
                getLogger().severe("Failed to load map: " + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
            }
        });

        registerEvents();

        this.getServer().getCommandMap().register("game", new GameExecute());
        this.getServer().getCommandMap().register("map", new MapExecute());
        this.getServer().getCommandMap().register("s", new ShopExecute());
        this.getServer().getCommandMap().register("l", new LastMenuExecute());

        for (Material mat : Material.values()) {
            if (!mat.isItem()) continue;
            try {
                new ItemStack(mat);
            } catch (Exception ignored) {}
        }
        getLogger().info("ItemStack preloaded.");

        Bukkit.getPluginManager().registerEvents(new CustomEnchantListener(), this);
        CustomEnchantRegistry.register(new Adrenaline(this));

        timer = new GameTimer();
        timer.runTaskTimer(this, 20, 20);

        getLogger().info("CSCReworked Enabled.");
    }


    private void registerEvents() {
        PluginManager mn = Bukkit.getPluginManager();

        mn.registerEvents(new CancelListener(), this);
        mn.registerEvents(new JoinListener(), this);
        mn.registerEvents(new PotionListener(), this);

    }

    @Override
    public void onDisable() {

        for(Player p: Bukkit.getOnlinePlayers())
            p.kick();

        BossBar.removeAll();

        getLogger().info("CSCReworked Disabled.");
    }

    public String getLanguage(String path) {return language.getString(path);}
    public List<String> getLanguageList(String path) {return language.getStringList(path);}
    public String[] getLanguageArray(String path) {
        ArrayList<String> arrList = (ArrayList<String>) Main.getInstance().getLanguageList(path);
        String[] array = new String[arrList.size()];
        for(int i = 0; i < array.length; i++)
            array[i] = arrList.get(i);
        return array;
    }

}
