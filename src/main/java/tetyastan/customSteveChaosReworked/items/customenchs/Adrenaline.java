package tetyastan.customSteveChaosReworked.items.customenchs;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.attribute.Attribute;

import java.util.Objects;

@Getter
public class Adrenaline implements CustomEnchant {

    private final NamespacedKey key;

    public Adrenaline(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "Adrenaline");
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public String getName() {
        return "Адреналин";
    }

    @Override
    public boolean canApply(ItemStack item) {
        return item.getType().name().endsWith("_SWORD");
    }

    @Override
    public void onAttack(Player attacker, Entity victim, int level) {
        double baseMaxHealth = Objects.requireNonNull(attacker.getAttribute(Attribute.MAX_HEALTH)).getValue();
        PotionEffect current = attacker.getPotionEffect(PotionEffectType.HEALTH_BOOST);
        int currentAmplifier = current != null ? current.getAmplifier() : -1;
        int attackCount = currentAmplifier + 1;

        if (attackCount >= 10) return;

        double factor = 1.0 - ((double) attackCount / 10.0);
        double bonusHealth = level * 2.0 * factor;

        if (attacker.getHealth() < baseMaxHealth) {
            attacker.heal(bonusHealth);
            return;
        }

        int duration = 20 * 5;

        attacker.addPotionEffect(new PotionEffect(
                PotionEffectType.HEALTH_BOOST,
                duration,
                attackCount,
                true, true
        ));

        attacker.heal(bonusHealth);
    }
}