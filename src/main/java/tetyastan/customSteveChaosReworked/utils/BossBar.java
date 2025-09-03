package tetyastan.customSteveChaosReworked.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import tetyastan.customSteveChaosReworked.Main;

import java.util.UUID;

@Getter
public class BossBar {

    private static final Map<String, BossBar> bars = new HashMap<>();

    private final org.bukkit.boss.BossBar bar;
    private final Map<UUID, Player> players = new HashMap<>();
    private final Main instance;
    private String title;

    public BossBar(Main instance, String title) {
        this.instance = instance;
        this.title = title;
        this.bar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
        this.bar.setVisible(true);
        bars.put(title, this);
    }

    public static void removeAll() {
        for (BossBar b : bars.values()) {
            b.remove();
        }
        bars.clear();
    }

    public void addPlayer(Player p) {
        if (players.containsKey(p.getUniqueId())) return;
        players.put(p.getUniqueId(), p);
        bar.addPlayer(p);
    }

    public void removePlayer(Player p) {
        if (!players.containsKey(p.getUniqueId())) return;
        players.remove(p.getUniqueId());
        bar.removePlayer(p);
    }

    public void setTitle(String title) {
        this.title = title;
        bar.setTitle(title);
    }

    public void setProgress(double progress) {
        bar.setProgress(Math.max(0, Math.min(1, progress)));
    }

    public void remove() {
        for (Player p : players.values()) {
            bar.removePlayer(p);
        }
        bar.setVisible(false);
        players.clear();
        bars.values().remove(this);
    }
}