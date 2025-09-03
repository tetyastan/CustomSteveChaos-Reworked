package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkSwordmanListener implements Listener {
	
	@EventHandler
	private void damage(EntityDamageByEntityEvent e) {
		if(e.getDamager().getType() != EntityType.PLAYER) return;
		CustomPlayer p = Game.getInstance().getPlayer(e.getDamager().getUniqueId());
		if(p.getPerk() == null || p.getPerk() != Perk.SWORDMAN) return;
		
		e.setDamage(e.getDamage() + (e.getDamage()*0.15));
		
	}
	
}
