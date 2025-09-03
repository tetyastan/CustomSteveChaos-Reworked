package tetyastan.customSteveChaosReworked.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import tetyastan.customSteveChaosReworked.Main;
import tetyastan.customSteveChaosReworked.utils.Chat;

public class MapExecute extends Command {

	public MapExecute() {
		super("map");
		this.setDescription("Map command");
		this.setUsage("/map <setlobby|arena|duel|reload>");
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, String @NotNull [] args) {
		if (!(sender instanceof Player player)) return true;
		if (!sender.hasPermission("customstevechaos.admin")) {
			Chat.NO_PERM.send(sender);
			return true;
		}

		if (args.length == 0) {
			help(player);
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "setlobby" -> setLobby(player);
			case "arena" -> {
				if (args.length < 2) { help(player); return true; }
				switch (args[1].toLowerCase()) {
					case "create" -> arenaCreate(player, args);
					case "remove" -> arenaRemove(player, args);
					case "list" -> arenaList(player);
					case "setspawn" -> arenaSetSpawn(player, args);
					case "setspawnmob" -> arenaSetSpawnMob(player, args);
					default -> help(player);
				}
			}
			case "duel" -> {
				if (args.length < 2) { help(player); return true; }
				switch (args[1].toLowerCase()) {
					case "create" -> duelCreate(player, args);
					case "remove" -> duelRemove(player, args);
					case "list" -> duelList(player, args);
					case "setp1" -> duelSetp1(player, args);
					case "setp2" -> duelSetp2(player, args);
					default -> help(player);
				}
			}
			case "reload" -> reload(player);
			default -> help(player);
		}

		return true;
	}

	private void duelList(Player p, String[] args) {

        StringBuilder str = new StringBuilder();
		for(String _str: Main.getInstance().getDuelConfig().getMaps())
			str.append("&9&l").append(_str).append("&8; ");
		
		Chat.INFO.send(p, str.toString());
		
	}

	private void duelRemove(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getDuelConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundDuel"));
			return;
		}
		
		Main.getInstance().getDuelConfig().remove(args[2]);
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.duelRemoved"));
		
	}

	private void duelCreate(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(Main.getInstance().getDuelConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.duelAlreadyCreated"));
			return;
		}
		
		Main.getInstance().getDuelConfig().create(args[2], p.getLocation(), p.getLocation(), p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.duelCreated"));
		
	}

	private void arenaList(Player p) {
		
		StringBuilder str = new StringBuilder();
		for(String _str: Main.getInstance().getArenasConfig().getArenas())
			str.append("&9&l").append(_str).append("&8; ");
		
		Chat.INFO.send(p, str.toString());
		
	}

	private void arenaRemove(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getArenasConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundArena"));
			return;
		}
		
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.arenaRemoved"));
		Main.getInstance().getArenasConfig().remove(args[2]);
		
	}

	private void reload(Player p) {
		
		Map.getInstance().reload();
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.mapReloaded"));
		
	}

	private void help(Player p) {
		
		Chat.INFO.send(p, Main.getInstance().getLanguageArray("messages.info.helpMap"));
		
	}

	private void duelSetp2(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getDuelConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundDuel"));
			return;
		}
		
		Main.getInstance().getDuelConfig().setLP2(args[2], p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.locChanged"));
		
	}

	private void duelSetp1(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getDuelConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundDuel"));
			return;
		}
		
		Main.getInstance().getDuelConfig().setLP1(args[2], p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.locChanged"));
		
	}

	private void arenaSetSpawnMob(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getArenasConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundArena"));
			return;
		}
		
		Main.getInstance().getArenasConfig().setSpawnMob(args[2], p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.locChanged"));
		
	}

	private void arenaSetSpawn(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(!Main.getInstance().getArenasConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notFoundArena"));
			return;
		}
		
		Main.getInstance().getArenasConfig().setSpawn(args[2], p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.locChanged"));
		
	}

	private void arenaCreate(Player p, String[] args) {
		if(args.length < 3) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.notEnoughArgs"));
			return;
		} else if(Main.getInstance().getArenasConfig().contains(args[2])) {
			Chat.FAIL.send(p, Main.getInstance().getLanguage("messages.fail.arenaAlreadyCreated"));
			return;
		}
		
		Main.getInstance().getArenasConfig().create(args[2], p.getLocation(), p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.arenaCreated"));
		
	}

	private void setLobby(Player p) {
		
		Main.getInstance().getMapConfig().setLobby(p.getLocation());
		Chat.SUCCESS.send(p, Main.getInstance().getLanguage("messages.success.lobbySetted"));
		
	}

}
