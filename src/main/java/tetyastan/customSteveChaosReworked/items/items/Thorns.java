package tetyastan.customSteveChaosReworked.items.items;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

public class Thorns extends Item {
	
	public static Thorns item = new Thorns();
	

	protected Thorns() {
		super("thorns");
	}

	public void cooldown(ItemStack stack) {
		if (stack == null) return;
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) return;

		NamespacedKey key = new NamespacedKey(Main.getInstance(), "cooldown");
		meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + 60000);

		stack.setItemMeta(meta);
	}

	public boolean isCooldown(ItemStack stack) {
		Long cooldownTime = ItemsUtil.getTagLong(stack, "cooldown");
		return cooldownTime != null && cooldownTime > System.currentTimeMillis();
	}
	
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		Entity ent = e.getEntity();
		if(!uuids.contains(ent.getUniqueId())) return;
		
		e.setCancelled(true);
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile) {
			ProjectileSource shooter = ((Projectile)damager).getShooter();
			if(shooter instanceof LivingEntity) damager = (LivingEntity)shooter;
		}
		if(!(damager instanceof LivingEntity le)) return;
		le.damage(e.getDamage()/2);
		double thornsDamage = e.getDamage() / 2;

        le.damage(thornsDamage, e.getEntity());

    }
	
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.thorns.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.thorns.lore"));
		
	}
	
	private final ArrayList<UUID> uuids = new ArrayList<>();
	@Override
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(isCooldown(e.getItem())) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.cooldown"));
			return;
		}
		
		uuids.add(p.getUniqueId());
		new BukkitRunnable() {
			
			@Override
			public void run() {uuids.remove(p.getUniqueId());}
			
		}.runTaskLater(Main.getInstance(), 80);
		cooldown(Objects.requireNonNull(e.getItem()));
		Chat.INFO.send(p, Main.getInstance().getLanguage("items.thorns.used"));
		
	}

	@Override
	public boolean onInteractEntity(Player player, ItemStack item, Entity target) {
		return false;
	}

}
