package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

@Getter
public class PerkSelectEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	@Setter
	private Perk perk;
	private final CustomPlayer customPlayer;
	
    public PerkSelectEvent(Perk perk, CustomPlayer customPlayer) {
    	
    	this.perk = perk;
    	this.customPlayer = customPlayer;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
