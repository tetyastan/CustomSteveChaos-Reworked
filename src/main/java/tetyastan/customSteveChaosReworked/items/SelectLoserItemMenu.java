package tetyastan.customSteveChaosReworked.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.Menu;

import java.util.*;

public class SelectLoserItemMenu extends Menu {

	private final HashMap<Integer, ItemStack> items = new HashMap<>();
	private boolean selected = false;

	public SelectLoserItemMenu() {
		super(Main.getInstance().getLanguage("menus.selectItem"), 27, true);
		populateItems();
	}

	private void populateItems() {
		items.clear();
		for (int num : Arrays.asList(10, 13, 16)) {
			ItemStack item = Items.generateLoser(Game.getInstance().getWave().getWave());
			this.items.put(num, item);
			inv.setItem(num, item);
		}
	}

	@Override
	public boolean onClick(Player player, ItemStack clickedItem, int slot, ClickType click) {

		ItemStack item = items.get(slot);
		if (item != null) {
			player.getInventory().addItem(item);
			Chat.SUCCESS.send(player, Main.getInstance().getLanguage("messages.success.itemSelected"));
			selected = true;
			player.closeInventory();
		}

		return true;
	}

	@Override
	public void onClose(Player player) {
		if (selected) return;

		List<ItemStack> list = new ArrayList<>(items.values());
		ItemStack item = list.get(new Random().nextInt(list.size()));
		player.getInventory().addItem(item);
		Chat.SUCCESS.send(player, Main.getInstance().getLanguage("messages.success.itemSelected"));
	}
}