package tetyastan.customSteveChaosReworked.items.customenchs;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantRegistry {

    private static final List<CustomEnchant> CUSTOM_ENCHANTS = new ArrayList<>();

    public static void register(CustomEnchant enchant) {
        CUSTOM_ENCHANTS.add(enchant);
    }

    public static List<CustomEnchant> getAll() {
        return CUSTOM_ENCHANTS;
    }
}
