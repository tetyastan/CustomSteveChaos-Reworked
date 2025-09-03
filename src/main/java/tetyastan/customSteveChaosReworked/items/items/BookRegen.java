package tetyastan.customSteveChaosReworked.items.items;

import java.util.HashMap;
import java.util.UUID;

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

public class BookRegen extends Item {
	
	public static BookRegen item = new BookRegen();

	protected BookRegen() {
		super("bookRegen");
	}
	
	@EventHandler
	public void regen(EntityRegainHealthEvent e) {
		if(e.getEntityType() != EntityType.PLAYER || !uuids.containsKey(e.getEntity().getUniqueId())) return;
		
		Player p = (Player)e.getEntity();
		Game.getInstance().getPlayer(p.getUniqueId()).regen(uuids.get(p.getUniqueId()));
		
	}
	
	@EventHandler
	public void gameEnd(GameStopEvent e) {uuids.clear();}
	
	
	@Override
	public void onCreate(ItemStack stack) {
		
		ItemsUtil.setName(stack, Main.getInstance().getLanguage("items.bookRegen.name"));
		ItemsUtil.setLore(stack, Main.getInstance().getLanguageArray("items.bookRegen.lore"));
		
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

		Chat.INFO.send(p, Main.getInstance().getLanguage("items.bookRegen.used"));
	}

	@Override
	public boolean onInteractEntity(Player player, ItemStack item, Entity target) {
		return false;
	}

}
