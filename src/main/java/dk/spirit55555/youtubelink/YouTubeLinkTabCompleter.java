package dk.spirit55555.youtubelink;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class YouTubeLinkTabCompleter implements TabCompleter {
	private YouTubeLink plugin;
	
	//The command options
	private String[] options = {
		"version",
		"reload",
		"permissions",
		"change",
		"remove"
	};
	
	public YouTubeLinkTabCompleter(YouTubeLink instance) {
		plugin = instance;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("youtubeadmin")) {
			if (args.length == 1)
				return getSuggestions(args[0], options);
		}
		
		else if (cmd.getName().equalsIgnoreCase("youtubechannel") && args.length == 1 && sender instanceof Player) {
			Player player = (Player) sender;
			
			List<String> suggestion = new ArrayList<String>();
			suggestion.add(plugin.links.getConfig().getString(player.getUniqueId().toString(), null));
			return suggestion;
		}
		
		return null;
	}
	
	/**
	 * Helper function for getting the right suggestions.
	 * @param option String of what the user has already written.
	 * @param suggestions Array of suggestions that should be checked.
	 * @return The suggestions that match.
	 */
	private List<String> getSuggestions(String option, String[] suggestions) {
		List<String> newSuggestions = new ArrayList<String>();

		for (String suggestion : suggestions) {
			//Only show suggestions that could match what the user has already written.
			if (suggestion.matches("^" + option + ".+")) {
				newSuggestions.add(suggestion);
			}
		}

		return newSuggestions;
	}
}