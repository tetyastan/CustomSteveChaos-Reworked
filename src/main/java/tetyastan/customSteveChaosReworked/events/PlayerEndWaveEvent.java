package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

@Getter
public class PlayerEndWaveEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final CustomPlayer customPlayer;
	private final int wave;
	@Setter
	private int deposit;
	private final boolean duel;
	
    public PlayerEndWaveEvent(CustomPlayer p, int wave, int deposit, boolean duel) {
    	
    	customPlayer = p;
    	this.wave = wave;
    	this.deposit = deposit;
    	this.duel = duel;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
