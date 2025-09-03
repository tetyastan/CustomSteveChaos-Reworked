package tetyastan.customSteveChaosReworked.players.perks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkArcherListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkAssassinListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkBerserkListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkDodgerListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkGamblingListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkGladiatorListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkLuckyListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkMasterWeaponListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkSwordmanListener;
import tetyastan.customSteveChaosReworked.players.perks.listeners.PerkTitanListener;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

@Getter
public enum Perk {
	
	BERSERK(Material.IRON_AXE, "perks.berserk.name", "perks.berserk.lore", new PerkBerserkListener()),
	SWORDMAN(Material.IRON_SWORD, "perks.swordman.name", "perks.swordman.lore", new PerkSwordmanListener()),
	ARCHER(Material.BOW, "perks.archer.name", "perks.archer.lore", new PerkArcherListener()),
	LUCKY(Material.GOLD_INGOT, "perks.lucky.name", "perks.lucky.lore", new PerkLuckyListener()),
	GLADIATOR(Material.WOODEN_SWORD, "perks.gladiator.name", "perks.gladiator.lore", new PerkGladiatorListener()),
	TITAN(Material.IRON_BLOCK, "perks.titan.name", "perks.titan.lore", new PerkTitanListener()),
	DODGER(Material.FEATHER, "perks.dodger.name", "perks.dodger.lore", new PerkDodgerListener()),
	MASTER_WEAPON(Material.ANVIL, "perks.masterweapon.name", "perks.masterweapon.lore", new PerkMasterWeaponListener()),
	ASSASSIN(Material.LEATHER_BOOTS, "perks.assassin.name", "perks.assassin.lore", new PerkAssassinListener()),
	GAMBLING(Material.GOLD_NUGGET, "perks.gambling.name", "perks.gambling.lore", new PerkGamblingListener());
	
	private final ItemStack item;
	private final String name;
	private final String[] description;
	
	Perk(Material mat, String pathName, String pathDescription, Listener listener) {
		
		name = Main.getInstance().getLanguage(pathName);
		description = Main.getInstance().getLanguageArray(pathDescription);
		item = ItemsUtil.generateItem(mat, name, description);
		
		Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
	}
	
}
