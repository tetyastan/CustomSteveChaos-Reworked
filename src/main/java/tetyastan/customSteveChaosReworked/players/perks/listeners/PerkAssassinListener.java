package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tetyastan.customSteveChaosReworked.events.PerkSelectEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkAssassinListener implements Listener {
	
	@EventHandler
	private void move(PerkSelectEvent e) {
		CustomPlayer p = e.getCustomPlayer();
		org.bukkit.entity.Player _p = p.getBP();
		if(e.getPerk() != Perk.ASSASSIN) return;
		
		_p.setWalkSpeed((float)(_p.getWalkSpeed() + (_p.getWalkSpeed()*0.4)));
		
	}
	
}
