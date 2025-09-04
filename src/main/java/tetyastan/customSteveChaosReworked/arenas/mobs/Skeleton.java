package tetyastan.customSteveChaosReworked.arenas.mobs;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Skeleton extends Mob {
	
	public static Skeleton mob = new Skeleton();
	

	public Skeleton() {
		super("skeleton");
	}
	
	@Override
	public boolean onSpawn(LivingEntity ent) {
		EntityEquipment equip = ent.getEquipment();
        assert equip != null;
        equip.setHelmet(ItemStack.of(Material.LEATHER_HELMET));
		equip.setChestplate(null);
		equip.setLeggings(null);
		equip.setBoots(null);

		return false;
	}

}
