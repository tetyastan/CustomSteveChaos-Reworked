package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkGladiatorListener implements Listener {
	
	@EventHandler
	private void win(PlayerEndWaveEvent e) {
		CustomPlayer p = e.getCustomPlayer();
		if(p.getPerk() != Perk.GLADIATOR || !e.isDuel()) return;
		
		e.setDeposit((int)(e.getDeposit() + (e.getDeposit()*0.45)));
		
	}
	
}
