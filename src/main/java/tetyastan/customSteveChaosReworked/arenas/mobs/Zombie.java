package tetyastan.customSteveChaosReworked.arenas.mobs;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Zombie extends Mob {
	
	public static Zombie mob = new Zombie();
	
	
	public Zombie() {
		super("zombie");
	}


	@Override
	public boolean onSpawn(LivingEntity ent) {

		EntityEquipment equip = ent.getEquipment();
		if (equip != null) {
			equip.setItem(EquipmentSlot.HAND, null);
			equip.setItem(EquipmentSlot.OFF_HAND, null);
			equip.setHelmet(ItemStack.of(Material.LEATHER_HELMET));
			equip.setChestplate(null);
			equip.setLeggings(null);
			equip.setBoots(null);
		}

		return false;
	}
}
