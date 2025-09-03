package tetyastan.customSteveChaosReworked.players.perks.listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.perks.Perk;

import java.util.Objects;

public class PerkBerserkListener implements Listener {

	@EventHandler
	private void damage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof org.bukkit.entity.Player _p)) return;

		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		if (p.getPerk() != Perk.BERSERK) return;

		double maxHealth = Objects.requireNonNull(_p.getAttribute(Attribute.MAX_HEALTH)).getValue();
		double procent = _p.getHealth() / maxHealth;

		if (procent < 0.425) {
			e.setDamage(e.getDamage() * 1.1);
		}
	}

	@EventHandler
	private void speed(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof org.bukkit.entity.Player _p)) return;

		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		if (p.getPerk() != Perk.BERSERK) return;

		double maxHealth = Objects.requireNonNull(_p.getAttribute(Attribute.MAX_HEALTH)).getValue();
		double procent = _p.getHealth() / maxHealth;

		float baseSpeed = 0.2f;
		if (procent < 0.425) {
			baseSpeed *= 1.1f;
		}
		_p.setWalkSpeed(baseSpeed);
	}


}
