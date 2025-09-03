package tetyastan.customSteveChaosReworked.duels;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class RatesMenu extends Menu {
	
	private final Menu from;
	private final Duel duel;
	private final CustomPlayer target;
	
	public RatesMenu(Duel duel, CustomPlayer p, CustomPlayer target, Menu from) {
		super(Main.getInstance().getLanguage("menus.rates.name").replace("%player%", target.getBP().getName()), 27, true);
		
		this.duel = duel;
		this.target = target;
		
		this.from = from;
		
		inv.setItem(0, ItemsUtil.generateItem(Material.BIRCH_SIGN, Main.getInstance().getLanguage("menus.rates.back")));
		inv.setItem(2, ItemsUtil.generateItem(Material.BLACK_STAINED_GLASS_PANE, (byte)5, "+50"));
		inv.setItem(3, ItemsUtil.generateItem(Material.BLACK_STAINED_GLASS_PANE, (byte)5, "+100"));
		inv.setItem(5, ItemsUtil.generateItem(Material.BLACK_STAINED_GLASS_PANE, (byte)5, "+200"));
		inv.setItem(6, ItemsUtil.generateItem(Material.BLACK_STAINED_GLASS_PANE, (byte)5, "+500"));
		inv.setItem(13, ItemsUtil.generateItem(Material.GLOWSTONE_DUST, Main.getInstance().getLanguage("menus.rates.costRate").replace("%money%", duel.getRate(p) + "")));
		
	}
	
	
	@Override
	public boolean onClick(org.bukkit.entity.Player _p, ItemStack item, int slot, ClickType click) {
		
		if(slot == 0) from.open(_p);
		else {
			
			CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
			int cost = switch (slot) {
                case 2 -> 50;
                case 3 -> 100;
                case 5 -> 200;
                case 6 -> 500;
                default -> 0;
            };
            if(duel.getP1().equals(target)) duel.addRate1(p, cost);
			else if(duel.getP2().equals(target)) duel.addRate2(p, cost);
			
			inv.setItem(13, ItemsUtil.generateItem(Material.GLOWSTONE_DUST, Main.getInstance().getLanguage("menus.rates.costRate").replace("%money%", duel.getRate(p) + "")));
			
		}
		
		return true;
	}
	
}
