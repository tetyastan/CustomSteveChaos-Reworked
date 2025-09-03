package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

@Getter
public class DuelStopEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final Duel duel;
	private final CustomPlayer winner;
	
    public DuelStopEvent(Duel duel, CustomPlayer winner) {
    	
    	this.duel = duel;
    	this.winner = winner;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
