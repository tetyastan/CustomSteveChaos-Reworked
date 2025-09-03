package tetyastan.customSteveChaosReworked.arenas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.attribute.Attribute;

import lombok.Getter;
import lombok.Setter;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.events.PlayerEndWaveEvent;
import tetyastan.customSteveChaosReworked.game.Game;
import tetyastan.customSteveChaosReworked.map.Map;
import tetyastan.customSteveChaosReworked.players.CustomPlayer;

public class Arena implements Listener {

	@Getter @Setter
	private CustomPlayer pl;
	@Getter
	private String name;
	@Getter
	private Location spawn, spawnMob;
	private final ArrayList<LivingEntity> mobs = new ArrayList<>();
	private boolean spawning = false;

	public Arena(String name, Location spawn, Location spawnMob) {

		this.name = name;
		this.spawn = spawn;
		this.spawnMob = spawnMob;

		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}

	public void remove() {

		EntityDamageEvent.getHandlerList().unregister(this);
		EntityDeathEvent.getHandlerList().unregister(this);

		pl = null;

	}

	public void spawnMobs(PackMobs pack) {
		if (!isDone()) return;

		int wave = Game.getInstance().getWave().getWave();
		spawning = true;

		spawn.getWorld().setDifficulty(Difficulty.EASY);
		spawn.getWorld().setSpawnFlags(false, false);

		for (LivingEntity ent : spawn.getWorld().getLivingEntities()) {
			if (!Arrays.asList(EntityType.ARMOR_STAND, EntityType.PLAYER).contains(ent.getType())) {
				ent.remove();
			}
		}

		new BukkitRunnable() {
			int current = 0;

			@Override
			public void run() {
				if (current >= pack.getMobs().length) {
					this.cancel();
					spawning = false;

					if (mobs.isEmpty()) {
						end();
					}
					return;
				}

				Mobs mob = pack.getMobs()[current];
				LivingEntity ent = mob.spawn(spawnMob);

				double baseHealth = 2;
				double health = baseHealth * Math.pow(1.19, wave);
				Objects.requireNonNull(ent.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(health);
				ent.setHealth(health);

				double baseDamage = 1.2;
				double damage = baseDamage * Math.pow(1.15, wave);
				if (ent.getAttribute(Attribute.ATTACK_DAMAGE) != null) {
					Objects.requireNonNull(ent.getAttribute(Attribute.ATTACK_DAMAGE)).setBaseValue(damage);
				}

				mobs.add(ent);

				if (ent instanceof Creature creature) {
					creature.setTarget(pl.getBP());
				}

				current++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 15);
	}

	private void end() {
		new BukkitRunnable() {
			@Override
			public void run() {
				pl.getBP().teleport(Map.getInstance().getLobby());
				int deposit = Game.getInstance().getWave().getWave() * 50 + 150;
				PlayerEndWaveEvent event = new PlayerEndWaveEvent(pl, Game.getInstance().getWave().getWave(), deposit, false);
				Bukkit.getPluginManager().callEvent(event);
				deposit = event.getDeposit();
				pl.deposit(deposit);
				if (!(
					pl.getBP().getInventory().contains(Material.WOODEN_SWORD) ||
							pl.getBP().getInventory().contains(Material.STONE_SWORD) ||
							pl.getBP().getInventory().contains(Material.IRON_SWORD) ||
							pl.getBP().getInventory().contains(Material.GOLDEN_SWORD) ||
							pl.getBP().getInventory().contains(Material.DIAMOND_SWORD) ||
							pl.getBP().getInventory().contains(Material.NETHERITE_SWORD)))
				{
					pl.getBP().getInventory().addItem(ItemStack.of(Material.STONE_SWORD));
				}
			}
		}.runTaskLater(Main.getInstance(), 5);
	}

	@EventHandler
	public void onProjectileDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Projectile proj)) return;
		if (!(proj.getShooter() instanceof LivingEntity shooter)) return;
		if (!mobs.contains(shooter)) return;

		int wave = Game.getInstance().getWave().getWave();
		double baseDamage = 1.2;
		double damage = baseDamage * Math.pow(1.15, wave);

		e.setDamage(damage);
	}

	public void boostMobs() {
		for(LivingEntity ent: mobs) {

			Objects.requireNonNull(ent.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(
					Objects.requireNonNull(ent.getAttribute(Attribute.MAX_HEALTH)).getBaseValue() * 1.003);

			Objects.requireNonNull(ent.getAttribute(Attribute.ATTACK_DAMAGE)).setBaseValue(
					Objects.requireNonNull(ent.getAttribute(Attribute.ATTACK_DAMAGE)).getBaseValue() * 1.002);

			Objects.requireNonNull(ent.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(
					Objects.requireNonNull(ent.getAttribute(Attribute.MOVEMENT_SPEED)).getBaseValue() * 1.002);

			if(!(Objects.requireNonNull(ent.getAttribute(Attribute.SCALE)).getBaseValue() >= 1.5))
				Objects.requireNonNull(ent.getAttribute(Attribute.SCALE)).setBaseValue(
					Objects.requireNonNull(ent.getAttribute(Attribute.SCALE)).getBaseValue() * 1.002);

			ent.heal(Objects.requireNonNull(ent.getAttribute(Attribute.MAX_HEALTH)).getBaseValue()/12);
		}
	}

	public void stop() {
		if(isDone()) return;

		ArrayList<LivingEntity> mobs = new ArrayList<>(this.mobs);
		for(LivingEntity ent: mobs)
			ent.remove();

		Duel duel = Duel.getInstance();
		if(pl != null && pl.getBP() != null && !pl.isSpec()) pl.getBP().teleport(duel.isStart() ? duel.getMap().getLView() : Map.getInstance().getLobby());

	}

	public boolean isDone() {return !spawning && mobs.isEmpty();}


	@EventHandler
	private void deathMob(EntityDeathEvent e) {
		if(!mobs.contains(e.getEntity())) return;

		e.getDrops().clear();
		e.setDroppedExp(0);

		mobs.remove(e.getEntity());

		if (mobs.isEmpty() && !spawning) {
			end();
		}
	}

	@EventHandler
	private void playerDied(EntityDamageEvent e) {
		if(e.getEntityType() != EntityType.PLAYER || isDone()) return;
		Player _p = (Player)e.getEntity();
		if(_p.getHealth() > e.getDamage()) return;
		CustomPlayer p = Game.getInstance().getPlayer(_p.getUniqueId());
		if(p.getArena() != this) return;

		if(!p.removeLife()) {

			p.regen(1000);
			_p.teleport(spawn);

		} else {

			PlayerEndWaveEvent event = new PlayerEndWaveEvent(p, Game.getInstance().getWave().getWave(), 0, false);
			Bukkit.getPluginManager().callEvent(event);

			stop();

		}

		e.setCancelled(true);
	}

}