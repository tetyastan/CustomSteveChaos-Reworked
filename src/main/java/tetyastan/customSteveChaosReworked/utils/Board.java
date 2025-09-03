package tetyastan.customSteveChaosReworked.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class Board {
	
	@Getter
	private Scoreboard board;
	private CustomPlayer customPlayer;
	private final String title;
	private final @NotNull BukkitTask task;

	public Board(CustomPlayer p, String title) {
		this.customPlayer = p;
		this.title = title;
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();

		Objective obj = board.registerNewObjective(title, "dummy", Component.text(title));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		p.getBP().setScoreboard(board);

		task = new BukkitRunnable() {
			@Override
			public void run() {
				update();
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
	}
	
	public void remove() {
		
		task.cancel();
		customPlayer.getBP().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		for(String str: board.getEntries())
			board.resetScores(str);
		
		customPlayer.removeBoard();
		customPlayer = null;
		
	}
	
	private void addLines(String... lines) {
		
		Objective obj = board.getObjective(title);
		for(int i = 0; i < lines.length; i++) {
            assert obj != null;
            obj.getScore(lines[i]).setScore(lines.length-i-1);
        }
		
	}
	
	public void update() {
		if(!Game.getInstance().isStart()) return;
		
		for(String str: board.getEntries())
			board.resetScores(str);
		
		String[] lines = Main.getInstance().getLanguageArray("scoreboard.lines");
		for(int i = 0; i < lines.length; i++) {
			
			Perk perk = customPlayer.getPerk();
			
			lines[i] = lines[i].replace("%money%", "" + customPlayer.getMoney());
			lines[i] = lines[i].replace("%wave%", "" + Game.getInstance().getWave().getWave());
			lines[i] = lines[i].replace("%players%", "" + Game.getInstance().getNotSpecPlayers().size());
			lines[i] = lines[i].replace("%perk%", perk == null ? "" : perk.getName());
			
		}
		
		addLines(lines);
	}
	
}
