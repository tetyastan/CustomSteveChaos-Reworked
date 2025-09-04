package tetyastan.customSteveChaosReworked.arenas.mobs;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import org.bukkit.inventory.EquipmentSlot;
import tetyastan.customSteveChaosReworked.utils.ItemsUtil;

public class ZombieArmor extends Mob {
	
	public static ZombieArmor mob = new ZombieArmor();
	

	public ZombieArmor() {
		super("zombie_armor");
	}
	
	
	@Override
	public boolean onSpawn(LivingEntity ent) {
		
		EntityEquipment equip = ent.getEquipment();
        assert equip != null;
        equip.setItem(EquipmentSlot.HAND, null);
		equip.setItem(EquipmentSlot.OFF_HAND, null);
		equip.setHelmet(ItemsUtil.generateItem(Material.IRON_HELMET, ""));
		equip.setChestplate(ItemsUtil.generateItem(Material.LEATHER_CHESTPLATE, ""));
		equip.setBoots(ItemsUtil.generateItem(Material.LEATHER_BOOTS, ""));
		
		return false;
	}
	
}
