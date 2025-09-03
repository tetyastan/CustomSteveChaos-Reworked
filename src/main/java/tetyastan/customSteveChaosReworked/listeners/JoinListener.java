package tetyastan.customSteveChaosReworked.listeners;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class JoinListener implements Listener {

	@EventHandler
	private void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);

		Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(20.0);
		p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).getValue());

		p.setFoodLevel(20);
		p.setSaturation(20f);
		p.setExp(0);
		p.setLevel(0);
		p.setWalkSpeed(0.2f);

		p.getActivePotionEffects().forEach(eff -> p.removePotionEffect(eff.getType()));
	}


}
