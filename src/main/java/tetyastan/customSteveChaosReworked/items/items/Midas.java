package tetyastan.customSteveChaosReworked.items.items;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

public class Midas extends Item {
	
	public static Midas item = new Midas();
	

	protected Midas() {
		super("midas");
	}

	public void cooldown(ItemStack stack) {
		if (stack == null) return;
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) return;

		NamespacedKey key = new NamespacedKey(Main.getInstance(), "cooldown");
		meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + 95000);

		stack.setItemMeta(meta);
	}


	public boolean isCooldown(ItemStack stack) {
		Long cooldownTime = ItemsUtil.getTagLong(stack, "cooldown");
		return cooldownTime != null && cooldownTime > System.currentTimeMillis();
	}
	
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.midas.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.midas.lore"));
		
	}
	
	@Override
	public boolean onInteractEntity(Player _p, ItemStack item, Entity _ent) {
		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		if(!Game.getInstance().isStart() || !(_ent instanceof LivingEntity ent) || _ent.getType() == EntityType.PLAYER || _ent.getType() == EntityType.ARMOR_STAND) return false;
		if(isCooldown(item)) {
			Chat.FAIL.send(p.getBP(), Main.getInstance().getLanguage("messages.fail.cooldown"));
			return false;
		}
        ent.damage(ent.getHealth());
		
		p.deposit(190);
		cooldown(item);
		Chat.INFO.send(ent, Main.getInstance().getLanguage("items.midas.used"));
		
		return false;
	}
	
}
