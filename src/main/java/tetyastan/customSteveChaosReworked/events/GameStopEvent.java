package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

@Getter
public class GameStopEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final Game game;
	private final CustomPlayer winner;
	
    public GameStopEvent(Game game, CustomPlayer winner) {
    	
    	this.game = game;
    	this.winner = winner;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
