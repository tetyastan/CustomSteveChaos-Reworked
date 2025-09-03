package tetyastan.customSteveChaosReworked.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.arenas.PackMobs;

@Getter
public class WaveStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	public static HandlerList getHandlerList() {return handlers;}
	
	private final int wave;
	private final PackMobs pack;
	@Setter
	private boolean cancelled = false;
	
    public WaveStartEvent(int wave, PackMobs pack) {
    	
    	this.wave = wave;
    	this.pack = pack;
    	
    }
    
    public @NotNull HandlerList getHandlers() {return handlers;}
	
}
