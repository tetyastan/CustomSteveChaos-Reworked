package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.duels.Duel;

@Getter
public class DuelStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final Duel duel;
	@Setter
	private boolean cancelled = false;
	
    public DuelStartEvent(Duel duel) {
    	
    	this.duel = duel;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
