package tetyastan.customSteveChaosReworked.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tetyastan.customSteveChaosReworked.items.customenchs.CustomEnchant;
import tetyastan.customSteveChaosReworked.items.customenchs.CustomEnchantRegistry;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;
import tetyastan.customSteveChaosReworked.utils.MiscUtil;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Items {

	private static final Random random = new Random();

	private static final List<Material> MATERIAL_TIERS = Arrays.asList(
			Material.WOODEN_SWORD, Material.STONE_SWORD,
			Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
			Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
			Material.GOLDEN_SWORD, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE,
			Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,

			Material.IRON_SWORD, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
			Material.IRON_LEGGINGS, Material.IRON_BOOTS,

			Material.DIAMOND_SWORD, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
			Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,

			Material.NETHERITE_SWORD, Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE,
			Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
	);

	private static final Map<Material, List<Enchantment>> ENCHANT_CACHE = new HashMap<>();

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
		Material mat = getMaterialForWave(wave);
		ItemStack item = new ItemStack(mat, 1);
		return generateEnchs(item, wave);
	}

	public static ItemStack generateLoser(int wave) {
		return generate(wave + 3);
	}

	private static Material getMaterialForWave(int wave) {
		if (wave < 1) wave = 1;
		if (wave > 30) wave = 30;

		List<Material> pool = new ArrayList<>();

		if (wave <= 5) {
			pool.addAll(MATERIAL_TIERS.subList(0, 10));
		} else if (wave <= 15) {
			pool.addAll(MATERIAL_TIERS.subList(0, 15));
		} else if (wave <= 25) {
			pool.addAll(MATERIAL_TIERS.subList(0, 20));
		} else {
			pool.addAll(MATERIAL_TIERS);
		}

		return pool.get(random.nextInt(pool.size()));
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

		List<CustomEnchant> customEnchants = CustomEnchantRegistry.getAll().stream()
				.filter(e -> e.canApply(item))
				.toList();

		if (!customEnchants.isEmpty()) {
			CustomEnchant chosen = customEnchants.get(random.nextInt(customEnchants.size()));
			int deviation = random.nextInt(deviationRange + 1) - (deviationRange / 2);
			int level = Math.max(1, baseLevel + deviation);

			List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            assert lore != null;
            lore.add(ItemsUtil.color("&7" + chosen.getName() + " " + MiscUtil.toRoman(level) + " (&aCSC&7)"));
			meta.setLore(lore);

			NamespacedKey key = chosen.getKey();
			meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
		}

		item.setItemMeta(meta);
		ItemsUtil.unbreakable(item);
		ItemsUtil.addToLore(item, "&7Получен на волне: &a" + wave);

		return item;
	}
}