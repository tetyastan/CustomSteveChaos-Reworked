package tetyastan.customSteveChaosReworked.items.customenchs;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CustomEnchantListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player attacker)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (!weapon.hasItemMeta()) return;

        ItemMeta meta = weapon.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (CustomEnchant enchant : CustomEnchantRegistry.getAll()) {
            NamespacedKey key = enchant.getKey();
            if (pdc.has(key, PersistentDataType.INTEGER)) {
                Integer level = pdc.get(key, PersistentDataType.INTEGER);
                if (level != null && level > 0) {
                    enchant.onAttack(attacker, e.getEntity(), level);
                }
            }
        }
    }
}