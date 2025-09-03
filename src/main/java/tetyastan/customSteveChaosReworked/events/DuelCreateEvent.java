package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

@Setter
@Getter
public class DuelCreateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private CustomPlayer p1, p2;
	private boolean cancelled = false;
	
    public DuelCreateEvent(CustomPlayer p1, CustomPlayer p2) {
    	
    	this.p1 = p1;
    	this.p2 = p2;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
