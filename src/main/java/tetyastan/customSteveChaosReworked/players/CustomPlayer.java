package tetyastan.customSteveChaosReworked.players;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.arenas.Arena;
import tetyastan.customSteveChaosReworked.players.perks.Perk;
import tetyastan.customSteveChaosReworked.utils.Board;
import tetyastan.customSteveChaosReworked.utils.Chat;
import org.bukkit.attribute.Attribute;

@Getter
public class CustomPlayer implements Listener {
	
	private final UUID uuid;
	@Setter
	private Perk perk;
	private Arena arena;
	private int money = 0, lives = 3;
	@Getter
	private InfoDuel infoDuel = new InfoDuel();
	@Getter
	private Board board;
	@Getter @Setter
	private boolean editor = false;
	
	public CustomPlayer(org.bukkit.entity.Player p) {this(p.getUniqueId());}
	public CustomPlayer(UUID uuid) {
		
		this.uuid = uuid;
		board = new Board(this, Main.getInstance().getLanguage("scoreboard.name"));
		
	}

	public void remove() {
		
		if(arena != null) {
			
			arena.stop();
			arena.setPl(null);
			arena = null;
		
		}
		removeBoard();
		
	}
	
	public void removeBoard() {
		if(board == null) return;
		
		Board _board = board;
		board = null;
		_board.remove();
		
	}
	
	public org.bukkit.entity.Player getBP() {return Bukkit.getPlayer(uuid);}
	public boolean isSpec() {return editor || getBP().getGameMode() == GameMode.SPECTATOR;}
	public void enableSpec() {if(!isSpec()) getBP().setGameMode(GameMode.SPECTATOR);}
	
	public boolean isMoney(int money) {return this.money >= money;}
	public boolean withdraw(int money) {
		if(money < 1) return true;
		if(isMoney(money)) {
			this.money -= money;
			Chat.INFO.send(getBP(), Main.getInstance().getLanguage("messages.info.withdraw").replace("%money%", money + ""));
			return true;
		}
		else {Chat.INFO.send(getBP(), Main.getInstance().getLanguage("messages.fail.notEnoughMoney")); return false;}
	}
	public void deposit(int money) {
		if(money < 1) return;
		
		this.money += money;
		Chat.INFO.send(getBP(), Main.getInstance().getLanguage("messages.info.deposit").replace("%money%", money + ""));
		
	}
	
	public boolean removeLife() {
		if(lives < 1 || isSpec()) return false;
		
		if(--lives < 1) {
			
			enableSpec();
			Chat.INFO.sendAll(Main.getInstance().getLanguage("messages.info.playerLose").replace("%player%", getBP().getName()));
			
		} else Chat.INFO.sendAll(Main.getInstance().getLanguage("messages.info.playerLostLive").replace("%player%", getBP().getName()).replace("%lives%", lives + ""));

		return false;
	}
	public void addLife() {lives++;}
	
	public void setArena(Arena arena) {
		
		this.arena = arena;
		arena.setPl(this);
		
	}

	public void regen(double health) {
		if (health <= 0) return;

		org.bukkit.entity.Player bp = getBP();
		if (bp == null) return;

		double maxHealth = Objects.requireNonNull(bp.getAttribute(Attribute.MAX_HEALTH)).getValue();
		double newHealth = Math.min(bp.getHealth() + health, maxHealth);
		bp.setHealth(newHealth);
	}

	public void addMaxHealth(double health) {
		if (health <= 0) return;

		org.bukkit.entity.Player bp = getBP();
		if (bp == null) return;

		double currentMax = Objects.requireNonNull(bp.getAttribute(Attribute.MAX_HEALTH)).getValue();
		Objects.requireNonNull(bp.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(currentMax + health);
		regen(health);
	}

	public void removeMaxHealth(double health) {
		if (health <= 0) return;

		org.bukkit.entity.Player bp = getBP();
		if (bp == null) return;

		double currentMax = Objects.requireNonNull(bp.getAttribute(Attribute.MAX_HEALTH)).getValue();
		double newMax = Math.max(currentMax - health, 1.0);
		Objects.requireNonNull(bp.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(newMax);

		if (bp.getHealth() > newMax) bp.setHealth(newMax);
	}
	
	@Override
	public boolean equals(Object p) {
		
		if(!(p instanceof CustomPlayer)) return false;
		
		return ((CustomPlayer)p).uuid.equals(uuid);
		
	}
	
}
