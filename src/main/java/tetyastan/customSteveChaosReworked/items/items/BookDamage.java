package tetyastan.customSteveChaosReworked.items.items;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.events.GameStopEvent;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

public class BookDamage extends Item {
	
	public static BookDamage item = new BookDamage();

	protected BookDamage() {
		super("book_damage");
	}
	
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		boolean bow = false;
		Entity damager = e.getDamager();
		if(damager instanceof Projectile) {
			ProjectileSource shooter = ((Projectile)damager).getShooter();
			if(shooter instanceof LivingEntity) {damager = (LivingEntity)shooter; bow = true;}
		}
		if(!(damager instanceof LivingEntity)) return;
		if(!uuids.containsKey(damager.getUniqueId())) return;
		
		e.setDamage(e.getDamage() + uuids.get(damager.getUniqueId())*(bow ? 2 : 4));
		
	}
	
	@EventHandler
	public void gameEnd(GameStopEvent e) {uuids.clear();}
	
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.bookDamage.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.bookDamage.lore"));
		
	}
	
	private final HashMap<UUID, Integer> uuids = new HashMap<>();
	@Override
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Game.getInstance().isStart()) return;

		uuids.put(p.getUniqueId(), uuids.getOrDefault(p.getUniqueId(), 0) + 1);

		EquipmentSlot hand = e.getHand();
        assert hand != null;
        ItemStack stack = p.getInventory().getItem(hand);

        if (stack.getAmount() == 1) {
			p.getInventory().setItem(hand, null);
		} else {
			stack.setAmount(stack.getAmount() - 1);
			p.getInventory().setItem(hand, stack);
		}

		Chat.INFO.send(p, Main.getInstance().getLanguage("items.bookDamage.used"));
	}

	@Override
	public boolean onInteractEntity(Player player, ItemStack item, Entity target) {
		return false;
	}

}
