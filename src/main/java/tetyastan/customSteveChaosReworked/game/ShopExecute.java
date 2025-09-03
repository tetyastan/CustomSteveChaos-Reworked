package tetyastan.customSteveChaosReworked.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.items.shop.ShopMenu;

public class ShopExecute extends Command {

    public ShopExecute() {
        super("s");
        this.setDescription("Shop command");
        this.setUsage("/s");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        shop(sender);
        return true;
    }

    private void shop(CommandSender sender) {
        if (sender instanceof Player player && Game.getInstance().getStatus() == Status.WAITING_WAVE) {
            new ShopMenu().open(player);
        }
    }
}