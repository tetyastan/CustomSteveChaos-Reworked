package tetyastan.customSteveChaosReworked.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import org.jetbrains.annotations.Nullable;
import tetyastan.customSteveChaosReworked.Main;

public class ItemsUtil {

	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static void setLore(ItemStack item, String... lines) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.stream(lines).map(ItemsUtil::color).toList());
		item.setItemMeta(meta);
	}

	public static void insertLoreLines(ItemStack item, int afterLines, String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> oldLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		List<String> lore = new ArrayList<>(oldLore.subList(0, Math.min(afterLines, oldLore.size())));
		Arrays.stream(lines).map(ItemsUtil::color).forEach(lore::add);
		lore.addAll(oldLore.subList(Math.min(afterLines, oldLore.size()), oldLore.size()));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static void unbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setUnbreakable(true);
			item.setItemMeta(meta);
		}
	}

	public static ItemStack addToLore(ItemStack item, String... newLines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		Arrays.stream(newLines).map(ItemsUtil::color).forEach(lore::add);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static void setName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(color(name));
		item.setItemMeta(meta);
	}

	public static void changeLore(ItemStack item, int line, String newText) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		if (line < lore.size()) {
			lore.set(line, color(newText));
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	public static ItemStack setGlow(ItemStack item, boolean glow) {
		if (glow) item.addUnsafeEnchantment(Enchantment.EFFICIENCY, 1);
		else item.removeEnchantment(Enchantment.EFFICIENCY);
		ItemMeta meta = item.getItemMeta();
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack generateItem(Material type, int amount, String name, String... lines) {
		ItemStack item = new ItemStack(type, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(color(name));
		meta.setLore(Arrays.stream(lines).map(ItemsUtil::color).toList());
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack generateItem(Material type, String name, String... lines) {
		return generateItem(type, 1, name, lines);
	}

	public static ItemStack generateSkull(String playerName, String displayName, String... lines) {
		ItemStack skull = new ItemStack(Material.SKELETON_SKULL, 1, (byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(playerName);
		meta.setDisplayName(color(displayName));
		meta.setLore(Arrays.stream(lines).map(ItemsUtil::color).toList());
		skull.setItemMeta(meta);
		return skull;
	}

	public static String getFriendlyName(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return ChatColor.stripColor(item.getItemMeta().getDisplayName());
		}
		return "";
	}

	public static String getName(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		}
		return "";
	}

	public static @Nullable ItemMeta setTag(ItemStack item, String key, String value) {
		ItemMeta meta = item.getItemMeta();
		meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, value);
		item.setItemMeta(meta);
		return meta;
	}

	public static String getTag(ItemStack item, String key) {
		if (!item.hasItemMeta()) return "";
		ItemMeta meta = item.getItemMeta();
		String val = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING);
		return val == null ? "" : val;
	}

	public static Long getTagLong(ItemStack item, String key) {
		if (!item.hasItemMeta()) return null;
		ItemMeta meta = item.getItemMeta();
		return meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), key), PersistentDataType.LONG);
	}
}