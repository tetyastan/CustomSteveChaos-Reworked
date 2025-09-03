package tetyastan.customSteveChaosReworked.items.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

import java.util.Objects;

@Getter
public abstract class Item implements Listener {

	private final String id;

	protected Item(String id) {
		this.id = id;
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}

	public ItemStack generateItem(Material material) {
		ItemStack stack = ItemsUtil.generateItem(material, "");
		stack.setItemMeta(ItemsUtil.setTag(stack, "id", id));
		onCreate(stack);
		return stack;
	}

	protected boolean isThisItem(ItemStack stack) {
		return stack == null
				|| stack.getType() == Material.AIR
				|| !Objects.equals(id, ItemsUtil.getTag(stack, "id"));
	}

	private boolean hasCustomItemInHands(LivingEntity entity) {
		if (entity.getEquipment() == null) return true;
		ItemStack main = entity.getEquipment().getItem(EquipmentSlot.HAND);
		ItemStack off = entity.getEquipment().getItem(EquipmentSlot.OFF_HAND);
		return isThisItem(main) && isThisItem(off);
	}

	private LivingEntity getDamagerEntity(Entity entity) {
		if (entity instanceof Projectile proj) {
			ProjectileSource shooter = proj.getShooter();
			if (shooter instanceof LivingEntity le) return le;
		} else if (entity instanceof LivingEntity le) {
			return le;
		}
		return null;
	}

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		if (isThisItem(item)) return;
		onInteract(e);
	}

	@EventHandler
	public void interactEntity(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if (hasCustomItemInHands(player)) return;
		e.setCancelled(onInteractEntity(player, e.getRightClicked()));
	}

	@EventHandler
	public void hitEntity(EntityDamageByEntityEvent e) {
		LivingEntity damager = getDamagerEntity(e.getDamager());
		if (damager == null || hasCustomItemInHands(damager)) return;
		onHitEntity(e);
	}

	@EventHandler
	public void pickUp(EntityPickupItemEvent e) {
		if (!(e.getEntity() instanceof Player player)) return;
		ItemStack stack = e.getItem().getItemStack();
		if (isThisItem(stack)) return;
		e.setCancelled(onPickup(player, e.getItem()));
	}

	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		ItemStack stack = e.getCurrentItem();
		if (isThisItem(stack)) return;
		onClickInventory(e);
	}

	@EventHandler
	public void selectHotbar(PlayerItemHeldEvent e) {
		ItemStack newItem = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (isThisItem(newItem)) return;
		e.setCancelled(onSelectHotbar(newItem, e.getNewSlot(), e.getPreviousSlot()));
	}

	protected void onCreate(ItemStack stack) {}

	protected void onInteract(PlayerInteractEvent e) {}

	protected boolean onInteractEntity(Player player, Entity target) { return false; }

	protected void onHitEntity(EntityDamageByEntityEvent e) {}

	protected boolean onDrop(Player player, org.bukkit.entity.Item item) { return false; }

	protected boolean onPickup(Player player, org.bukkit.entity.Item item) { return false; }

	protected void onClickInventory(InventoryClickEvent e) {}

	protected boolean onSelectHotbar(ItemStack stack, int newSlot, int prevSlot) { return false; }

	public abstract boolean onInteractEntity(Player player, ItemStack item, Entity target);
}