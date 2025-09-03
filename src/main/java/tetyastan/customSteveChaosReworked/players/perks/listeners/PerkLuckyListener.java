package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkLuckyListener implements Listener {
	
	@EventHandler
	private void endWave(PlayerEndWaveEvent e) {
		CustomPlayer p = e.getCustomPlayer();
		if(p.getPerk() != Perk.LUCKY || e.isDuel()) return;
		
		e.setDeposit((int)(e.getDeposit() + (e.getDeposit()*0.25)));
		
	}
	
}
