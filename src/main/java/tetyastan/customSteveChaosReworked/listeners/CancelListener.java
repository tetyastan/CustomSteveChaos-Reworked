package tetyastan.customSteveChaosReworked.listeners;

import java.util.Arrays;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.game.Status;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

public class CancelListener implements Listener {
	
	@EventHandler
	private void cancelDamagePlayer(EntityDamageEvent e) {
		if(e.getEntityType() != EntityType.PLAYER) return;
		CustomPlayer p = Game.getInstance().getPlayer(e.getEntity().getUniqueId());
		
		Duel duel = Duel.getInstance();
		if((duel.isMember(p) && duel.isStart()) || (p.getArena() != null && !p.getArena().isDone())) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	private void cancelRegen(EntityRegainHealthEvent e) {
		if(Arrays.asList(RegainReason.EATING, RegainReason.SATIATED).contains(e.getRegainReason()))
			e.setCancelled(true);
	}
	
	@EventHandler
	private void cancelHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	private void cancelPotion(PlayerItemConsumeEvent e) {
		CustomPlayer p = Game.getInstance().getPlayer(e.getPlayer().getUniqueId());
		
		if((Duel.getInstance().isMember(p) && Game.getInstance().getStatus() == Status.WAVE) || (p.getArena() != null && !p.getArena().isDone())) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	private void cancelDrop(PlayerDropItemEvent e) {
		if (Game.getInstance().getStatus() == Status.WAVE) {
			e.getItemDrop().remove();
		}
		else e.setCancelled(true);
	}
	
}
