package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

public class PerkDodgerListener implements Listener {
	
	@EventHandler
	private void damage(EntityDamageEvent e) {
		if(e.getEntityType() != EntityType.PLAYER) return;
		CustomPlayer p = Game.getInstance().getPlayer(e.getEntity().getUniqueId());
		if(p.getPerk() == null || p.getPerk() != Perk.DODGER || Math.random() > 0.2) return;
		
		e.setCancelled(true);
	}
	
}
