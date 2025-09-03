package tetyastan.customSteveChaosReworked.items.shop;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.items.items.*;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class ShopMenu extends Menu {
	
	private final HashMap<Integer, ItemStack> slots = new HashMap<>();
	private final HashMap<Integer, Integer> cost = new HashMap<>();
	
	public ShopMenu() {
		super(Main.getInstance().getLanguage("menus.shop.name"), 27, true);
		
		slots.put(1, Midas.item.generateItem(Material.GOLD_NUGGET));
		slots.put(2, Thorns.item.generateItem(Material.MAGMA_CREAM));
		slots.put(3, BookDamage.item.generateItem(Material.BOOK));
		slots.put(4, BookHealth.item.generateItem(Material.BOOK));
		slots.put(5, BookRegen.item.generateItem(Material.BOOK));
		slots.put(6, BookLife.item.generateItem(Material.BOOK));
		
		cost.put(1, 1200);
		cost.put(2, 1750);
		cost.put(3, 2000);
		cost.put(4, 2000);
		cost.put(5, 2000);
		cost.put(6, 4000);
		
		for(int slot: slots.keySet())
			inv.setItem(slot, ItemsUtil.addToLore(slots.get(slot).clone(), Main.getInstance().getLanguage("menus.shop.cost").replace("%money%", cost.get(slot) + "")));
		
	}
	
	public boolean onClick(Player _p, ItemStack _item, int slot, ClickType click) {
		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		ItemStack item = slots.get(slot);
		if(item != null) {
			int cost = this.cost.get(slot);
			if(p.withdraw(cost)) {
				
				_p.getInventory().addItem(item);
				Chat.SUCCESS.send(_p, Main.getInstance().getLanguage("messages.success.itemBought"));
				
				_p.closeInventory();
			}
			
		}
		
		return true;
	}
	
}
