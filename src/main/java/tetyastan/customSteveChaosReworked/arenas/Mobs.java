package tetyastan.customSteveChaosReworked.arenas;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import tetyastan.customSteveChaosReworked.arenas.mobs.Endermite;
import tetyastan.customSteveChaosReworked.arenas.mobs.Golem;
import tetyastan.customSteveChaosReworked.arenas.mobs.Skeleton;
import tetyastan.customSteveChaosReworked.arenas.mobs.Spider;
import tetyastan.customSteveChaosReworked.arenas.mobs.Zombie;
import tetyastan.customSteveChaosReworked.arenas.mobs.ZombieArmor;

public enum Mobs {
	
	ZOMBIE(EntityType.ZOMBIE), SKELETON(EntityType.SKELETON), SPIDER(EntityType.SPIDER), BLAZE(EntityType.BLAZE),
	IRON_GOLEM(EntityType.IRON_GOLEM), ENDERMITE(EntityType.ENDERMITE), ZOMBIE_ARMOR(EntityType.ZOMBIE),
	SILVERFISH(EntityType.SILVERFISH), CAVE_SPIDER(EntityType.CAVE_SPIDER);
	
	private final EntityType type;
	
	Mobs(EntityType type) {this.type = type;}
	
	public LivingEntity spawn(Location loc) {

        return switch (this) {
            case ENDERMITE -> Endermite.mob.spawn(type, loc);
            case IRON_GOLEM -> Golem.mob.spawn(type, loc);
            case ZOMBIE_ARMOR -> ZombieArmor.mob.spawn(type, loc);
            case SPIDER -> Spider.mob.spawn(type, loc);
            case ZOMBIE -> Zombie.mob.spawn(type, loc);
            case SKELETON -> Skeleton.mob.spawn(type, loc);
            default -> (LivingEntity) loc.getWorld().spawnEntity(loc, type);
        };
		
	}
	
}
