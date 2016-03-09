package dk.spirit55555.youtubelink;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class YouTubeLink extends JavaPlugin {
	protected final ConfigAccessor links = new ConfigAccessor(this, "links.yml");
	protected final Pattern channelRegex = Pattern.compile("UC[a-zA-Z-0-9_\\-]{22}");
	protected final Pattern userRegex    = Pattern.compile("[a-zA-Z-0-9_\\-]{6,20}");
	
	private final YouTubeLinkCommandExecutor commandExecutor = new YouTubeLinkCommandExecutor(this);
	private final YouTubeLinkTabCompleter tabCompleter       = new YouTubeLinkTabCompleter(this);
	private final YouTubeLinkPlayerListener playerListener   = new YouTubeLinkPlayerListener(this);
	
	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(playerListener, this);
		
		getCommand("youtube").setExecutor(commandExecutor);
		getCommand("youtube").setTabCompleter(tabCompleter);
		
		getCommand("youtubechannel").setExecutor(commandExecutor);
		getCommand("youtubechannel").setTabCompleter(tabCompleter);
		
		getCommand("youtubeadmin").setExecutor(commandExecutor);
		getCommand("youtubeadmin").setTabCompleter(tabCompleter);
	}
	
	/**
	 * Show a message to a user with the configured prefix.
	 * @param sender Who to send the message to.
	 * @param message The message to send.
	 */
	protected void showMessage(CommandSender sender, String message) {
		//Get the prefix
		String prefix = getConfig().getString("prefix");

		//Translate the colors
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
		message = ChatColor.translateAlternateColorCodes('&', message);

		//Send the message
		sender.sendMessage(prefix + message);
	}
	
	protected String getMessage(String message) {
		return getConfig().getString("messages." + message);
	}
	
	protected String getChannelMessage(Player player) {
		String channel = links.getConfig().getString(player.getUniqueId().toString());
		
		if (userRegex.matcher(channel).matches())
			return String.format(getMessage("show.user"), player.getName(), channel);
		else
			return String.format(getMessage("show.channel"), player.getName(), channel);
	}
}
