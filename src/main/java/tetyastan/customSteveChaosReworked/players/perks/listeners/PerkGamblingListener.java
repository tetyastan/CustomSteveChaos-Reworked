package tetyastan.customSteveChaosReworked.players.perks.listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.events.DuelStopEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkGamblingListener implements Listener {
	
	@EventHandler
	private void duelStop(DuelStopEvent e) {
		Duel duel = e.getDuel();
		CustomPlayer winner = e.getWinner();
		HashMap<CustomPlayer, Integer> rates = duel.getP1().equals(winner) ? duel.getRate1() : duel.getRate2();
		for(Entry<CustomPlayer, Integer> set: rates.entrySet()) {
			if(set.getKey().getPerk() == null || set.getKey().getPerk() != Perk.GAMBLING) continue;
			
			set.setValue((int)(set.getValue() + (set.getValue()*0.2)));
			
		}
		
		
	}
	
}
