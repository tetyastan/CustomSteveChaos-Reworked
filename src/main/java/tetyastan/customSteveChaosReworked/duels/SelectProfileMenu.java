package tetyastan.customSteveChaosReworked.duels;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.players.ProfileMenu;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class SelectProfileMenu extends Menu {
	
	private final Duel duel;
	private final CustomPlayer p1, p2;
	
	public SelectProfileMenu(Duel duel) {
		super(Main.getInstance().getLanguage("menus.duel.name"), 36, false);
		
		this.duel = duel;
		p1 = duel.getP1();
		p2 = duel.getP2();
		
		String[] format = Main.getInstance().getLanguageArray("menus.duel.loreFormat");
		
		inv.setItem(13, ItemsUtil.generateItem(Material.ANVIL, Main.getInstance().getLanguage("menus.duel.nameArena").replace("%arena%", duel.getMap().getName())));
		
		String[] lore = format.clone();
		for(int i = 0; i < lore.length; i++)
			lore[i] = lore[i].replace("%wins%", p1.getInfoDuel().getWins() + "").replace("%loses%", p1.getInfoDuel().getLoses() + "");
		
		inv.setItem(20, ItemsUtil.generateSkull(p1.getBP().getName(), p1.getBP().getName(), lore));
		
		lore = format.clone();
		for(int i = 0; i < lore.length; i++)
			lore[i] = lore[i].replace("%wins%", p2.getInfoDuel().getWins() + "").replace("%loses%", p2.getInfoDuel().getLoses() + "");
		
		inv.setItem(24, ItemsUtil.generateSkull(p2.getBP().getName(), p2.getBP().getName(), lore));
		
	}
	
	
	@Override
	public boolean onClick(org.bukkit.entity.Player _p, ItemStack item, int slot, ClickType click) {
		CustomPlayer p;
		if(slot == 20) p = p1;
		else if(slot == 24) p = p2;
		else return true;
		
		if(click.isLeftClick()) {
			if(duel.isMember(Game.getInstance().getPlayer(_p.getUniqueId()))) {
				Chat.FAIL.send(_p, Main.getInstance().getLanguage("messages.fail.youMemberDuel"));
				return true;
			}
			
			RatesMenu menu = new RatesMenu(duel, Game.getInstance().getPlayer(_p.getUniqueId()), p, this);
			menu.open(_p);
			
		} else if(click.isRightClick()) {
			
			ProfileMenu menu = new ProfileMenu(p, this);
			menu.open(_p);
			
		}
		
		return true;
	}

}
