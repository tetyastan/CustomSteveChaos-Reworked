package tetyastan.customSteveChaosReworked.players;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.utils.Chat;
import tetyastan.customSteveChaosReworked.utils.Menu;

public class LastMenuExecute extends Command {

    public LastMenuExecute() {
        super("l");
        this.setDescription("Last Menu command");
        this.setUsage("/l");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
        Menu getLastMenu = Menu.getLastMenu((Player) sender);

        if (getLastMenu != null) {
            getLastMenu.open((Player) sender);
            Chat.INFO.send(sender, Main.getInstance().getLanguage("messages.info.menuReopened"));
        } else {
            Chat.FAIL.send(sender, Main.getInstance().getLanguage("messages.fail.menuNoLast"));
        }

        return true;
    }
}