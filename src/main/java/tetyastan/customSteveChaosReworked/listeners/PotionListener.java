package tetyastan.customSteveChaosReworked.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

public class PotionListener implements Listener {
	
	private final HashMap<UUID, ArrayList<ItemStack>> potions = new HashMap<>();
	
	@EventHandler(ignoreCancelled = true)
	private void potion(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		Player p = e.getPlayer();
		if(item.getType() != Material.POTION) return;
		
		ArrayList<ItemStack> items = potions.containsKey(p.getUniqueId()) ? potions.get(p.getUniqueId()) : new ArrayList<>();
		items.add(item);
		
		potions.put(e.getPlayer().getUniqueId(), items);
		
		new BukkitRunnable() {@Override public void run() {p.getInventory().remove(Material.GLASS_BOTTLE);}}.runTaskLater(Main.getInstance(), 2);
		
	}
	
	@EventHandler
	private void endWave(PlayerEndWaveEvent e) {
		
		CustomPlayer p = e.getCustomPlayer();
		ArrayList<ItemStack> items = potions.get(p.getUuid());
		if(items == null) return;
		
		potions.remove(p.getBP().getUniqueId());
		for(ItemStack item: items)
			p.getBP().getInventory().addItem(item);
		
	}
	
}
