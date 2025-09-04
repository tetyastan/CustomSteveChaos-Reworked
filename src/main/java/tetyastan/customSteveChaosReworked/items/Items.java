package tetyastan.customSteveChaosReworked.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Items {

	private static final Random random = new Random();

	private static final List<Material> ALLOWED_ITEMS = new ArrayList<>();
	private static final Map<Material, List<Enchantment>> ENCHANT_CACHE = new HashMap<>();

	static {
		ALLOWED_ITEMS.addAll(Arrays.asList(
				Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
				Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD
		));

		for (Material mat : Material.values()) {
			if (mat.name().endsWith("_HELMET") ||
					mat.name().endsWith("_CHESTPLATE") ||
					mat.name().endsWith("_LEGGINGS") ||
					mat.name().endsWith("_BOOTS")) {
				ALLOWED_ITEMS.add(mat);
			}
		}
	}

	private static List<Enchantment> getEnchantmentsFor(Material mat) {
		if (ENCHANT_CACHE.containsKey(mat)) {
			return ENCHANT_CACHE.get(mat);
		}

		List<Enchantment> list = new ArrayList<>();
		try {
			ItemStack testItem = new ItemStack(mat);
			for (Enchantment ench : Enchantment.values()) {
				if (ench.canEnchantItem(testItem)
						&& ench != Enchantment.BINDING_CURSE
						&& ench != Enchantment.VANISHING_CURSE
						&& ench != Enchantment.INFINITY
						&& ench != Enchantment.KNOCKBACK
						&& ench != Enchantment.UNBREAKING
						&& ench != Enchantment.AQUA_AFFINITY
						&& ench != Enchantment.FEATHER_FALLING
						&& ench != Enchantment.FROST_WALKER
						&& ench != Enchantment.SOUL_SPEED
						&& ench != Enchantment.SILK_TOUCH) {
					list.add(ench);
				}
			}
		} catch (Exception ignored) { }

		ENCHANT_CACHE.put(mat, list);
		return list;
	}

	public static ItemStack generate(int wave) {
		Material mat = ALLOWED_ITEMS.get(random.nextInt(ALLOWED_ITEMS.size()));
		ItemStack item = new ItemStack(mat, 1);
		return generateEnchs(item, wave);
	}

	public static ItemStack generateLoser(int wave) {
        return generate(wave + 3);
	}

	public static ItemStack generateEnchs(ItemStack item, int wave) {
		if (wave <= 0) wave = 1;

		ItemMeta meta = item.getItemMeta();
		if (meta == null) return item;

		int baseLevel = Math.max(1, wave / 2);
		int deviationRange = wave / 2 + wave / 3;

		int enchantCount = Math.min(1 + wave / 5, Enchantment.values().length);

		List<Enchantment> possibleEnchants = new ArrayList<>(getEnchantmentsFor(item.getType()));
		if (possibleEnchants.isEmpty()) return item;

		possibleEnchants.removeIf(e -> e.equals(Enchantment.THORNS));
		Collections.shuffle(possibleEnchants, random);

		for (int i = 0; i < Math.min(enchantCount, possibleEnchants.size()); i++) {
			Enchantment enchant = possibleEnchants.get(i);
			int deviation = random.nextInt(deviationRange + 1) - (deviationRange / 2);
			int level = Math.max(1, baseLevel + deviation);
			meta.addEnchant(enchant, level, true);
		}

		item.setItemMeta(meta);
		ItemsUtil.unbreakable(item);
		ItemsUtil.addToLore(item, "&7Получен на волне: &a" + wave);

		return item;
	}
}