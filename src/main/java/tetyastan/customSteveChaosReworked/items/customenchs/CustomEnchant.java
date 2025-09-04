package tetyastan.customSteveChaosReworked.items.customenchs;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomEnchant {

    String getName();

    NamespacedKey getKey();

    boolean canApply(ItemStack item);

    void onAttack(Player attacker, Entity victim, int level);

}

