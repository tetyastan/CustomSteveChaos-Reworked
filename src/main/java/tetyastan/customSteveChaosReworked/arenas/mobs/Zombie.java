package tetyastan.customSteveChaosReworked.arenas.mobs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

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
			equip.setHelmet(null);
			equip.setChestplate(null);
			equip.setLeggings(null);
			equip.setBoots(null);
		}

		return false;
	}
	
}
