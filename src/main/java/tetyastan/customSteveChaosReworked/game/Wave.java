package tetyastan.customSteveChaosReworked.game;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.arenas.Arena;
import tetyastan.customSteveChaosReworked.arenas.PackMobs;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.events.WaveStartEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;

@Getter
public class Wave implements Listener {
	
	private Game game;
	private PackMobs pack;
	private int wave = 0;
	
	public Wave(Game game) {
		this.game = game;
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(game.getStatus() != Status.WAVE || game.getNotSpecPlayers().size() < 2 || Duel.getInstance().isCreate()) return;
				for(CustomPlayer p: game.getNotSpecPlayers())
					if(!p.getArena().isDone())
						return;
				
				Duel.getInstance().newDuel();

				Chat.INFO.sendAll(Main.getInstance().getLanguage("messages.info.readyForItem"));

				nextWave();
				Chat.SUCCESS.sendAll(Main.getInstance().getLanguage("messages.success.waveEnded"));
				
				game.setStatus(Status.WAITING_WAVE);
			}
			
		}.runTaskTimer(Main.getInstance(), 20, 20);
		
	}
	
	public void remove() {
		
		game = null;
		PlayerEndWaveEvent.getHandlerList().unregister(this);
		
	}
	
	public void nextWave() {
		
		pack = PackMobs.values()[new Random().nextInt(PackMobs.values().length)];
		wave++;
		
	}
	
	public void start() {
		if(game.getStatus() != Status.WAITING_WAVE) return;
		
		WaveStartEvent event = new WaveStartEvent(wave, pack);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		
		for(CustomPlayer p: game.getNotSpecPlayers()) {
			if(Duel.getInstance().isMember(p)) continue;

			Arena arena = p.getArena();
			p.getBP().teleport(arena.getSpawn());
			p.getArena().spawnMobs(pack);
			for(PotionEffect eff: p.getBP().getActivePotionEffects())
				p.getBP().removePotionEffect(eff.getType());
			
			String[] mess = Main.getInstance().getLanguageArray("messages.info.waveStarted");
			for(int i = 0; i < mess.length; i++)
				mess[i] = mess[i].replace("%wave%", wave + "");
			Chat.INFO.send(p.getBP(), mess);
			
		}
		
		game.setStatus(Status.WAVE);
	}
	
	
	@EventHandler
	public void waveEnded(PlayerEndWaveEvent e) {
		
		org.bukkit.entity.Player _p = e.getCustomPlayer().getBP();
		new BukkitRunnable() {
			@Override
			public void run() {
				if(_p != null)
					e.getCustomPlayer().regen(1000);
			}
		}.runTaskLater(Main.getInstance(), 5);

		for(PotionEffect eff: _p.getActivePotionEffects())
			_p.removePotionEffect(eff.getType());

		CustomPlayer loser = Duel.getInstance().getLoser();
		if (!e.getCustomPlayer().equals(loser)) {
			e.getCustomPlayer().addMaxHealth(2);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				game.giveItem(e.getCustomPlayer());
			}
		}.runTaskLater(Main.getInstance(), 60);
		
		if(!e.isDuel()) {
			Chat.INFO.sendAll(Main.getInstance().getLanguage("messages.info.playerWinWave").replace("%player%", _p.getName()));
		}
	}
}
