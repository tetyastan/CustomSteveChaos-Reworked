package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.game.Game;

@Getter
public class GameStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final Game game;
	@Setter
	private boolean cancelled = false;
	
    public GameStartEvent(Game game) {
    	
    	this.game = game;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
    
}
