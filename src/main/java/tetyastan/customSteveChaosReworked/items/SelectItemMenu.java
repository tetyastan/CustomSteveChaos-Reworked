package tetyastan.customSteveChaosReworked.items;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class SelectItemMenu extends Menu {

	private final HashMap<Integer, ItemStack> items = new HashMap<>();
	private final HashMap<UUID, Integer> rerollPriceMap = new HashMap<>();

	public SelectItemMenu(Player player) {
		super(Main.getInstance().getLanguage("menus.selectItem"), 45, false);
		this.saveOnClose = true;
		populateItems();
		addRerollButton(player);
	}

	private void populateItems() {
		items.clear();
		for (int num : Arrays.asList(10, 13, 16, 28, 31, 34)) {
			ItemStack item = Items.generate(Game.getInstance().getWave().getWave());
			this.items.put(num, item);
			inv.setItem(num, item);
		}
	}

	private void addRerollButton(Player player) {
		int currentPrice = rerollPriceMap.getOrDefault(player.getUniqueId(), 300);
		ItemStack rerollButton = ItemsUtil.generateItem(
				Material.GOLD_BLOCK,
				Main.getInstance().getLanguage("menus.reroll.name"),
				Main.getInstance().getLanguage("menus.reroll.cost").replace("%money%", String.valueOf(currentPrice))
		);
		inv.setItem(inv.getSize() - 1, rerollButton);

		player.updateInventory();
	}

	@Override
	public boolean onClick(Player player, ItemStack clickedItem, int slot, ClickType click) {
		CustomPlayer p = Game.getInstance().getPlayer(player.getUniqueId());

		if (slot == inv.getSize() - 1) {
			int currentPrice = rerollPriceMap.getOrDefault(player.getUniqueId(), 300);
			if (!p.withdraw(currentPrice)) {
				Chat.FAIL.send(player, Main.getInstance().getLanguage("messages.fail.notEnoughMoney"));
				return true;
			}
			rerollPriceMap.put(player.getUniqueId(), currentPrice + 200);
			populateItems();
			addRerollButton(player);
			return true;
		}

		ItemStack item = items.get(slot);
		if (item != null) {
			player.getInventory().addItem(item);
			Chat.SUCCESS.send(player, Main.getInstance().getLanguage("messages.success.itemSelected"));
			Menu.clearLastMenu(player);
			close(player);
		}
		return true;
	}
}