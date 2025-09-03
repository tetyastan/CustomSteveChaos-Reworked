package tetyastan.customSteveChaosReworked.game;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.map.Map;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.BossBar;
import tetyastan.customSteveChaosReworked.utils.Chat;

public class GameTimer extends BukkitRunnable {
	
	@Getter
	private BossBar bar = new BossBar(Main.getInstance(), Main.getInstance().getLanguage("bossbar.waitingGame"));
	private final Game game = Game.getInstance();
	private final Duel duel = Duel.getInstance();
	private BukkitTask boostTask = null;

	@Override
	public void run() {

		autoStart();
		autoWave();
		checkWin();
		boostMob();
		timerToBoost();
		
	}
	
	private boolean boostEnable = false;
	private void boostMob() {
		if(game.getStatus() != Status.WAVE || !boostEnable) {
			if (boostTask != null) {
				boostTask.cancel();
				boostTask = null;
			}
			boostEnable = false;
			return;
		}

		bar.setTitle(Main.getInstance().getLanguage("bossbar.mobBoost"));
		bar.setProgress(100);

		if (boostTask == null) {
			boostTask = new BukkitRunnable() {
				@Override
				public void run() {
					for (CustomPlayer p : game.getNotSpecPlayers()) {
						if (!p.getArena().isDone()) {
							p.getArena().boostMobs();
							checkWin();
						}
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 10);
		}

	}
	
	private int timeToBoost = 30;
	private void timerToBoost() {
		if(game.getStatus() != Status.WAVE || boostEnable) {timeToBoost = 30; return;}
		
		if(--timeToBoost < 1) boostEnable = true;
		else {
			
			bar.setTitle(Main.getInstance().getLanguage("bossbar.timerToBoost"));
			bar.setProgress(((double)timeToBoost)/30.0);
			
		}
		
	}

	private void checkWin() {
		if(!game.isStart()) return;
		if(game.getNotSpecPlayers().isEmpty()) game.stop();
		if(game.getNotSpecPlayers().size() != 1) return;
		
		for(CustomPlayer p: game.getPlayers())
			Chat.INFO.send(p.getBP(), Main.getInstance().getLanguage("messages.info.playerWin").replace("%player%", game.getNotSpecPlayers().getFirst().getBP().getName()));
		
		game.stop();
		
	}

	private int timeToWave = 30;
	private void autoWave() {
		if(game.getStatus() != Status.WAITING_WAVE) {
			timeToWave = 30;
			return;
		}
		
		if(--timeToWave < 1) {
			
			timeToWave = 30;
			game.getWave().start();
			duel.start();
			
		} else {
			
			bar.setTitle(Main.getInstance().getLanguage("bossbar.waitingWave").replace("%wave%", (game.getWave().getWave()) + "").replace("%nameWave%", game.getWave().getPack().getName()));
			bar.setProgress(((double)timeToWave)/30.0);
			
		}
		
	}
	
	private int timeToStart = 10;
	private void autoStart() {
		if(game.getStatus() != Status.WAITING_GAME) {
			timeToStart = 10;
			return;
		} else if(game.getNotSpecPlayers().size() < Main.getInstance().getMinPlayers()) {
			bar.setTitle(Main.getInstance().getLanguage("bossbar.waitingPlayers").replace("%players%", (Main.getInstance().getMinPlayers()-game.getNotSpecPlayers().size())+ ""));
			bar.setProgress(1.0);
			timeToStart = 10;
			return;
		}
		if(game.getNotSpecPlayers().size() == Map.getInstance().getArenas().size() && timeToStart > 10) timeToStart = 10;
		
		if(--timeToStart < 1) {timeToStart = 10; game.start();}
		else {
			
			bar.setTitle(Main.getInstance().getLanguage("bossbar.waitingGame"));
			bar.setProgress(((double)timeToStart)/10.0);
			
		}
		
	}

}
