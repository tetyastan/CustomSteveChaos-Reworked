package tetyastan.customSteveChaosReworked.players.perks;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.events.PerkSelectEvent;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class SelectPerkMenu extends Menu {
	
	private final HashMap<Integer, Perk> perks = new HashMap<>();
	
	public SelectPerkMenu() {
		super(Main.getInstance().getLanguage("menus.selectPerk"), 9, true);
		this.saveOnClose = false;
		
		List<Perk> perks = Arrays.asList(Perk.values());
		Collections.shuffle(perks);
		
		if(!perks.isEmpty()) this.perks.put(2, perks.get(0));
		if(perks.size() > 1) this.perks.put(4, perks.get(1));
		if(perks.size() > 2) this.perks.put(6, perks.get(2));
		for(Entry<Integer, Perk> set: this.perks.entrySet())
			inv.setItem(set.getKey(), set.getValue().getItem());
		
	}
	
	private void selectPerk(Player _p, Perk perk) {
		
		if(perk != null) {
			
			CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
			if(p.getPerk() != null) return;
			PerkSelectEvent event = new PerkSelectEvent(perk, p);
			Bukkit.getPluginManager().callEvent(event);
			p.setPerk(event.getPerk());
			
			Chat.SUCCESS.send(_p, Main.getInstance().getLanguage("messages.success.perkSelected").replace("%perk%", perk.getName()));

			close(_p);
		}
		
	}
	
	@Override
	public boolean onClick(Player _p, ItemStack item, int slot, ClickType click) {
		selectPerk(_p, perks.get(slot));
		Game.getInstance().giveItem(Game.getInstance().getPlayer(_p.getUniqueId()));
		
		return true;
	}

	@Override
	public void onClose(Player p) {
		CustomPlayer cp = Game.getInstance().getPlayer(p.getUniqueId());

		if (cp != null && cp.getPerk() == null && !perks.isEmpty()) {
			Perk randomPerk = (Perk) perks.values().toArray()[new Random().nextInt(perks.size())];
			selectPerk(p, randomPerk);
			Game.getInstance().giveItem(cp);
		}
	}

}
