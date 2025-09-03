package tetyastan.customSteveChaosReworked.items.items;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

public class BookHealth extends Item {
	
	public static BookHealth item = new BookHealth();

	protected BookHealth() {
		super("bookHealth");
	}
	
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.bookHealth.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.bookHealth.lore"));
		
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Game.getInstance().isStart()) return;

		Game.getInstance().getPlayer(p.getUniqueId()).addMaxHealth(4);

		EquipmentSlot hand = e.getHand();
        assert hand != null;
        ItemStack stack = p.getInventory().getItem(hand);

        if (stack.getAmount() == 1) {
			p.getInventory().setItem(hand, null);
		} else {
			stack.setAmount(stack.getAmount() - 1);
			p.getInventory().setItem(hand, stack);
		}

		Chat.INFO.send(p, Main.getInstance().getLanguage("items.bookHealth.used"));
	}

	@Override
	public boolean onInteractEntity(Player player, ItemStack item, Entity target) {
		return false;
	}

}
