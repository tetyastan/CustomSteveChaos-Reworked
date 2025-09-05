package tetyastan.customSteveChaosReworked.duels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.events.DuelCreateEvent;
import tetyastan.customSteveChaosReworked.events.DuelStartEvent;
import tetyastan.customSteveChaosReworked.events.DuelStopEvent;
import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.items.items.BookDamage;
import tetyastan.customSteveChaosReworked.items.items.LoserBook;
import tetyastan.customSteveChaosReworked.map.Map;
import tetyastan.customSteveChaosReworked.players.InfoDuel;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;

public class Duel implements Listener {
	@Getter
	private static Duel instance = new Duel();

	@Getter private CustomPlayer p1, p2;
	@Getter private HashMap<CustomPlayer, Integer> rate1 = new HashMap<>(), rate2 = new HashMap<>();
	@Getter private boolean start = false;
	@Getter private SelectProfileMenu menu;
	private double duelTimeDmg = 0;
	private BukkitTask timer;
	@Getter private DuelMap map;

	public boolean isCreate() { return menu != null; }

	public void newDuel() {
		if (Game.getInstance().getWave().getWave() < 3 || isCreate()) return;

		ArrayList<CustomPlayer> customPlayers = Game.getInstance().getNotSpecPlayers();

		if (customPlayers.size() == 2 && Game.getInstance().getWave().getWave() % 3 != 0) return;

		Collections.shuffle(customPlayers);

		DuelCreateEvent event = new DuelCreateEvent(customPlayers.get(0), customPlayers.get(1));
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;

		map = Main.getInstance().getDuelConfig().getRandMap();
		this.p1 = event.getP1();
		this.p2 = event.getP2();

		String[] info = Main.getInstance().getLanguageArray("messages.info.infoDuel");
		for (int i = 0; i < info.length; i++)
			info[i] = info[i]
					.replace("%p1%", p1.getBP().getName())
					.replace("%p2%", p2.getBP().getName())
					.replace("%arena%", map.getName());
		Chat.INFO.sendAll(info);

		menu = new SelectProfileMenu(this);

		String raw = Main.getInstance().getLanguage("messages.info.clickToRate");

		Component mainComponent = LegacyComponentSerializer.legacyAmpersand()
				.deserialize(raw)
				.clickEvent(ClickEvent.runCommand("/game duelProfiles"));

		for (CustomPlayer p : customPlayers) {
			p.getBP().sendMessage(mainComponent);
		}

		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}


	public void remove() {
		if (!isCreate()) return;
		start = false;

		rate1.clear();
		rate2.clear();
		p1 = null;
		p2 = null;
		menu.remove();
		menu = null;
		if (timer != null) timer.cancel();
		map = null;

		PlayerQuitEvent.getHandlerList().unregister(this);
		EntityDamageEvent.getHandlerList().unregister(this);
	}

	public void openMenu(CustomPlayer p) {
		if (start) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.duelAlreadyStarted"));
			return;
		} else if (!isCreate()) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.duelNotFound"));
			return;
		}

		menu.open(p.getBP());
	}

	public boolean isMember(CustomPlayer p) {
		return p.equals(p1) || p.equals(p2);
	}

	public void start() {
		duelTimeDmg = 0;
		if (start || !isCreate()) return;

		DuelStartEvent event = new DuelStartEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;

		start = true;
		boolean onlineP1 = p1.getBP().isOnline(), onlineP2 = p2.getBP().isOnline();
		if (!onlineP1 && !onlineP2) {
			remove();
			return;
		} else if (!onlineP1) { win(p2); return; }
		else if (!onlineP2) { win(p1); return; }

		p1.getBP().teleport(map.getLP1());
		p2.getBP().teleport(map.getLP2());

		timer = new BukkitRunnable() {
			@Override
			public void run() {
				duelTimeDmg = duelTimeDmg * 1.08;
				p1.getBP().damage(duelTimeDmg);
			}
		}.runTaskTimer(Main.getInstance(), 25 * 20, 40);
	}

	public int getBank() { return getBankRate1() + getBankRate2(); }
	public int getRate(CustomPlayer p) {
		if (rate1.containsKey(p)) return rate1.get(p);
		return rate2.getOrDefault(p, 0);
	}
	public int getBankRate1() {
		return rate1.values().stream().mapToInt(Integer::intValue).sum();
	}
	public int getBankRate2() {
		return rate2.values().stream().mapToInt(Integer::intValue).sum();
	}
	public void addRate1(CustomPlayer p, int money) {
		if (start || !isCreate()) return;

		if (rate2.containsKey(p)) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.alreadyRate"));
			return;
		}
		if (isMember(p)) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.youMemberDuel"));
			return;
		}

		if (p.withdraw(money))
			rate1.merge(p, money, Integer::sum);
	}
	public void addRate2(CustomPlayer p, int money) {
		if (start || !isCreate()) return;

		if (rate1.containsKey(p)) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.alreadyRate"));
			return;
		}
		if (isMember(p)) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.youMemberDuel"));
			return;
		}

		if (p.withdraw(money))
			rate2.merge(p, money, Integer::sum);
	}

	public void win(CustomPlayer p) {
		if (!p.equals(p1) && !p.equals(p2)) return;
		start = false;
		CustomPlayer lose = p.equals(p1) ? p2 : p1;

		Bukkit.getPluginManager().callEvent(new DuelStopEvent(this, p));

		if (p.equals(p1)) {
			double k = (getBank() != 0 && getBankRate1() != 0)
					? (double) getBank() / getBankRate1() : 1;
			rate1.forEach((player, val) -> player.deposit((int) (val * k)));
		} else {
			double k = (getBank() != 0 && getBankRate2() != 0)
					? (double) getBank() / getBankRate2() : 1;
			rate2.forEach((player, val) -> player.deposit((int) (val * k)));
		}

		lose.getInfoDuel().lose();
		p.getInfoDuel().win();

		if (Game.getInstance().getWave().getWave() >= 15 && Game.getInstance().getWave().getWave() <= 30) {
			InfoDuel info = lose.getInfoDuel();
			int wins = info.getWins();
			int loses = info.getLoses();
			int total = wins + loses;

			if (total > 0) {
				double winrate = (wins * 100.0) / total;
				if (winrate < 30.0) {
					lose.getBP().getInventory().addItem(BookDamage.item.generateItem(Material.BOOK));
					lose.getBP().getInventory().addItem(LoserBook.item.generateItem(Material.BOOK));
					Chat.INFO.send(lose.getBP(), Main.getInstance().getLanguage("messages.info.duelLoser"));
				}
			}
		}

		int wave = Game.getInstance().getWave().getWave();
		if (wave > 30) {
			Chat.INFO.send(lose.getBP(), Main.getInstance().getLanguage("messages.info.heartsLose"));
			int heartsToRemove = (wave - 10) / 10;
			lose.removeMaxHealth(heartsToRemove);
		}

		remove();

		int deposit = getBank() != 0
				? getBank() / (rate1.size() + rate2.size())
				: 0;
		PlayerEndWaveEvent event = new PlayerEndWaveEvent(p, Game.getInstance().getWave().getWave(), deposit, true);
		Bukkit.getPluginManager().callEvent(event);
		p.deposit(event.getDeposit());

		Chat.INFO.sendAll(Main.getInstance().getLanguage("messages.info.duelEnded")
				.replace("%player%", p.getBP().getName()));

		deposit = getBank() != 0
				? getBank() / (rate1.size() + rate2.size())
				: 0;
		event = new PlayerEndWaveEvent(lose, Game.getInstance().getWave().getWave(), deposit / 2, true);
		Bukkit.getPluginManager().callEvent(event);
		lose.deposit(event.getDeposit());

		p.getBP().teleport(Map.getInstance().getLobby());
		lose.getBP().teleport(Map.getInstance().getLobby());
	}

	@EventHandler
	public void playerWin(EntityDamageEvent e) {
		if (e.getEntityType() != EntityType.PLAYER || !start || !isCreate()) return;
		org.bukkit.entity.Player _p = (org.bukkit.entity.Player) e.getEntity();
		if (_p.getHealth() > e.getDamage()) return;
		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		if (!p.equals(p1) && !p.equals(p2)) return;

		win(p.equals(p1) ? p2 : p1);
		e.setCancelled(true);
	}

	public CustomPlayer getLoser() {
		if (!isCreate() || start) return null;
		if (p1.getInfoDuel().getLoses() > 0) return p1;
		if (p2.getInfoDuel().getLoses() > 0) return p2;
		return null;
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		if (!start || !isCreate()) return;

		CustomPlayer p = Game.getInstance().getPlayer(e.getPlayer().getUniqueId());
		if (p == null) return;

		if (!p.equals(p1) && !p.equals(p2)) return;

		p.enableSpec();
		win(p.equals(p1) ? p2 : p1);
	}
}
