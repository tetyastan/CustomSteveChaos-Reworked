package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tetyastan.customSteveChaosReworked.events.PerkSelectEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkTitanListener implements Listener {
	
	@EventHandler
	private void gameStart(PerkSelectEvent e) {
		if(e.getPerk() != Perk.TITAN) return;
		CustomPlayer p = e.getCustomPlayer();
		
		p.addMaxHealth(8);
		
	}
	
}
