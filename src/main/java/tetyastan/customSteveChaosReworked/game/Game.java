package tetyastan.customSteveChaosReworked.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.arenas.Arena;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.events.GameStartEvent;
import tetyastan.customSteveChaosReworked.events.GameStopEvent;
import tetyastan.customSteveChaosReworked.items.SelectItemMenu;
import tetyastan.customSteveChaosReworked.items.SelectLoserItemMenu;
import tetyastan.customSteveChaosReworked.map.Map;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.SelectPerkMenu;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class Game implements Listener {
	
	@Getter
	private static Game instance = new Game();
	
	
	private final HashMap<UUID, CustomPlayer> players = new HashMap<>();
	@Getter @Setter
	private Status status = Status.WAITING_GAME;
	@Getter
	private Wave wave;
	
	public Game() {Bukkit.getPluginManager().registerEvents(this, Main.getInstance());}
	
	public boolean isStart() {return status == Status.WAITING_WAVE || status == Status.WAVE;}
	public void start() {
		if(status != Status.WAITING_GAME || getNotSpecPlayers().size() < Main.getInstance().getMinPlayers()) return;
		
		GameStartEvent event = new GameStartEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		
		status = Status.WAITING_WAVE;
		wave = new Wave(this);
		wave.nextWave();
		
		ArrayList<Arena> arenas = Map.getInstance().getArenas();
		Collections.shuffle(arenas);
		int i = 0;
		for(CustomPlayer p: getNotSpecPlayers()) {
			
			SelectPerkMenu menu = new SelectPerkMenu();
			menu.open(p.getBP());
			p.setArena(arenas.get(i));
			p.getBP().getInventory().setItem(17, ItemsUtil.generateItem(Material.ARROW, ""));
			i++;
			
		}
	}
	
	public void stop() {
		if(!isStart()) return;
		status = Status.STOPPED;
		
		Bukkit.getPluginManager().callEvent(new GameStopEvent(this, getNotSpecPlayers().isEmpty() ? null : getNotSpecPlayers().getFirst()));
		
		Duel.getInstance().remove();
		
		for(Arena arena: Map.getInstance().getArenas())
			arena.stop();
		for(CustomPlayer p: players.values()) {
			Chat.INFO.send(p.getBP(), Main.getInstance().getLanguage("messages.info.gameStopped"));
			p.enableSpec();
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				wave.remove();
				wave = null;

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			}
		}.runTaskLater(Main.getInstance(), 100);
	}

	public void giveItem(CustomPlayer p) {
		if (!isStart() || p == null || p.isSpec()) return;

		Menu menu = new SelectItemMenu(p.getBP());
		menu.open(p.getBP());
	}

	public void giveLoserItem(CustomPlayer p) {
		if (!isStart() || p == null || p.isSpec()) return;

		Menu menu = new SelectLoserItemMenu();
		menu.open(p.getBP());
	}
	
	public CustomPlayer getPlayer(UUID uuid) {return players.get(uuid);}
	public ArrayList<CustomPlayer> getPlayers() {return new ArrayList<>(players.values());}
	public ArrayList<CustomPlayer> getNotSpecPlayers() {
		
		ArrayList<CustomPlayer> customPlayers = new ArrayList<>();
		for(CustomPlayer p: this.players.values())
			if(!p.isSpec())
				customPlayers.add(p);
		
		return customPlayers;
	}
	
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		org.bukkit.entity.Player _p = e.getPlayer();
		CustomPlayer p = new CustomPlayer(e.getPlayer());
		if(status != Status.WAITING_GAME) {
			if(!_p.hasPermission("customstevechaos.admin")) {
				_p.kick();
				return;
			} else p.enableSpec();
		} else if(Map.getInstance().getArenas().size()-1 < players.size()) {
			if(!_p.hasPermission("customstevechaos.admin")) {
				_p.kick();
				return;
			} else p.enableSpec();
		}
		
		_p.teleport(Map.getInstance().getLobby());
		players.put(p.getUuid(), p);
		Main.getInstance().getTimer().getBar().addPlayer(_p);
		if (!p.isSpec()) {
			String raw = Main.getInstance().getLanguage("messages.info.joinPlayer")
					.replace("%player%", _p.getName())
					.replace("%players%", String.valueOf(getNotSpecPlayers().size()))
					.replace("%maxPlayersCount%", String.valueOf(Map.getInstance().getArenas().size()));

			@NotNull TextComponent joinMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(raw);

			for (org.bukkit.entity.Player online : Bukkit.getOnlinePlayers()) {
				if (!online.equals(_p)) {
					online.sendMessage(joinMessage);
				}
			}
		}
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		CustomPlayer p = players.get(uuid);
		if(p == null) return;
		p.remove();
		
		players.remove(uuid);
		String raw = Main.getInstance().getLanguage("messages.info.quitPlayer")
				.replace("%player%", p.getBP().getName())
				.replace("%players%", String.valueOf(getNotSpecPlayers().size()))
				.replace("%maxPlayersCount%", String.valueOf(Map.getInstance().getArenas().size()));

		Component quitMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(raw);

		for (org.bukkit.entity.Player online : Bukkit.getOnlinePlayers()) {
			online.sendMessage(quitMessage);
		}
		
	}
	
}
