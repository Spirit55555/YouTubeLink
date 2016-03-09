package dk.spirit55555.youtubelink;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class YouTubeLinkCommandExecutor implements CommandExecutor {
	private YouTubeLink plugin;
	
	public YouTubeLinkCommandExecutor(YouTubeLink instance) {
		plugin = instance;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("youtube") && args.length == 1) {
			Player player = Bukkit.getPlayer(args[0]);
			
			//The player has to be online.
			if (player == null)
				plugin.showMessage(sender, plugin.getMessage("only-online.get"));
			
			else if (player.hasPermission("youtubelink.change") && plugin.links.getConfig().contains(player.getUniqueId().toString()))
				plugin.showMessage(sender, plugin.getChannelMessage(player));
			
			else
				plugin.showMessage(sender, String.format(plugin.getMessage("no-channel.player"), player.getName()));
				
			return true;
		}
		
		else if (cmd.getName().equalsIgnoreCase("youtubechannel") && args.length == 1) {
			//The console can't have a channel...
			if (!(sender instanceof Player))
				return false;
			
			Player player = (Player) sender;
			
			if (args[0] == "none") {
				plugin.links.getConfig().set(player.getUniqueId().toString(), null);
				plugin.showMessage(player, plugin.getMessage("removed.player"));
			}
			
			else {
				if (!(plugin.userRegex.matcher(args[0]).matches() || plugin.channelRegex.matcher(args[0]).matches())) {
					plugin.showMessage(player, plugin.getMessage("valid"));
					return true;
				}
				
				plugin.links.getConfig().set(player.getUniqueId().toString(), args[0]);
				plugin.showMessage(player, String.format(plugin.getMessage("changed.player"), args[0]));
			}
			
			plugin.links.saveConfig();
			
			return true;
		}
		
		else if (cmd.getName().equalsIgnoreCase("youtubeadmin") && args.length > 0 && args.length < 4) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v")) {
					String versionMessage = plugin.getMessage("version");
					versionMessage = String.format(versionMessage, plugin.getDescription().getVersion());
	
					plugin.showMessage(sender, versionMessage);
					return true;
				}
				
				else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
					plugin.reloadConfig();
					plugin.links.reloadConfig();
	
					String reloadMessage = plugin.getMessage("reload");
	
					plugin.showMessage(sender, reloadMessage);
	
					//If the reload was done by a player, show it in the console too
					if (sender instanceof Player)
						plugin.showMessage(Bukkit.getConsoleSender(), reloadMessage);
	
					return true;
				}
				
				else if (args[0].equalsIgnoreCase("permissions") || args[0].equalsIgnoreCase("p")) {
					List<Permission> permissions = plugin.getDescription().getPermissions();
					String permissionsList = "";

					for (Permission permission : permissions) {
						permissionsList += "\n&c" + permission.getName() + "\n&a- " + permission.getDescription();
					}

					plugin.showMessage(sender, plugin.getMessage("permissions") + permissionsList);

					return true;
				}
			}
			
			else if ((args[0].equalsIgnoreCase("change") || args[0].equalsIgnoreCase("c")) && args.length == 3) {
				Player player = Bukkit.getPlayer(args[1]);
				
				//The player has to be online.
				if (player == null) {
					plugin.showMessage(sender, plugin.getMessage("only-online.change"));
					return true;
				}
				
				if (plugin.userRegex.matcher(args[2]).matches() || plugin.channelRegex.matcher(args[2]).matches()) {
					plugin.links.getConfig().set(player.getUniqueId().toString(), args[2]);
					plugin.showMessage(sender, String.format(plugin.getMessage("changed.admin"), player.getName(), args[2]));
				}
				
				else
					plugin.showMessage(sender, plugin.getMessage("valid"));
				
				return true;
			}
			
			else if ((args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) && args.length == 2) {
				Player player = Bukkit.getPlayer(args[1]);
				
				//The player has to be online.
				if (player == null)
					plugin.showMessage(sender, plugin.getMessage("only-online.remove"));
				
				else if (plugin.links.getConfig().contains(player.getUniqueId().toString())) {
					plugin.links.getConfig().set(player.getUniqueId().toString(), null);
					plugin.showMessage(sender, String.format(plugin.getMessage("removed.admin"), player.getName()));
				}
				
				else
					plugin.showMessage(sender, String.format(plugin.getMessage("no-channel.admin"), player.getName()));
				
				return true;
			}
		}
		
		return false;
	}
}
