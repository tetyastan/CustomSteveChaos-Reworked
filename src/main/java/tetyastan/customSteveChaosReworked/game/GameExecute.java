package tetyastan.customSteveChaosReworked.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.duels.Duel;
import tetyastan.customSteveChaosReworked.items.shop.ShopMenu;
import tetyastan.customSteveChaosReworked.utils.Chat;

public class GameExecute extends Command {

	public GameExecute() {
		super("game");
		this.setDescription("Game command");
		this.setUsage("/game <start|stop|shop|duelProfiles>");
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
		if (args.length == 0) {
			help(sender);
		} else switch (args[0].toLowerCase()) {
			case "start" -> start(sender);
			case "stop" -> stop(sender);
			case "duelprofiles" -> duelProfiles(sender);
			case "shop" -> shop(sender);
			default -> help(sender);
		}
		return true;
	}

	private void help(CommandSender sender) {
		Chat.INFO.send(sender, Main.getInstance().getLanguageArray("messages.info.helpGame"));
	}

	private void shop(CommandSender sender) {
		if (sender instanceof Player player && Game.getInstance().getStatus() == Status.WAITING_WAVE) {
			new ShopMenu().open(player);
		}
	}

	private void duelProfiles(CommandSender sender) {
		if (sender instanceof Player player && Duel.getInstance().isCreate()) {
			Duel.getInstance().openMenu(Game.getInstance().getPlayer(player.getUniqueId()));
		}
	}

	private void stop(CommandSender sender) {
		if (sender.hasPermission("customstevechaos.admin")) {
			Game.getInstance().stop();
			Chat.SUCCESS.send(sender, Main.getInstance().getLanguage("messages.success.successStopped"));
		} else Chat.NO_PERM.send(sender);
	}

	private void start(CommandSender sender) {
		if (sender.hasPermission("customstevechaos.admin")) {
			if (Game.getInstance().getStatus() != Status.WAITING_GAME) {
				Chat.FAIL.send(sender, Main.getInstance().getLanguage("messages.fail.alreadyStarted"));
			} else if (Game.getInstance().getNotSpecPlayers().size() < Main.getInstance().getMinPlayers()) {
				Chat.FAIL.send(sender, Main.getInstance().getLanguage("messages.fail.notEnoughPlayers"));
			} else {
				Game.getInstance().start();
				Chat.SUCCESS.send(sender, Main.getInstance().getLanguage("messages.success.successStarted"));
			}
		} else Chat.NO_PERM.send(sender);
	}

}
