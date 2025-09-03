package tetyastan.customSteveChaosReworked.items.items;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.events.GameStopEvent;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

import java.util.HashMap;
import java.util.UUID;

public class BookLife extends Item {

	public static BookLife item = new BookLife();

	protected BookLife() {
		super("bookLife");
	}
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.bookLife.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.bookLife.lore"));
		
	}

	@Override
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Game.getInstance().isStart()) return;

		Game.getInstance().getPlayer(p.getUniqueId()).addLife();

		EquipmentSlot hand = e.getHand();
        assert hand != null;
        ItemStack stack = p.getInventory().getItem(hand);

        if (stack.getAmount() == 1) {
			p.getInventory().setItem(hand, null);
		} else {
			stack.setAmount(stack.getAmount() - 1);
			p.getInventory().setItem(hand, stack);
		}

		Chat.INFO.send(p, Main.getInstance().getLanguage("items.bookLife.used"));
	}

	@Override
	public boolean onInteractEntity(Player player, ItemStack item, Entity target) {
		return false;
	}

}
