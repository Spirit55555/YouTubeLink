package dk.spirit55555.youtubelink;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class YouTubeLinkPlayerListener implements Listener {
	private YouTubeLink plugin;

	public YouTubeLinkPlayerListener(YouTubeLink instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (player.hasPermission("youtubelink.change") && !plugin.links.getConfig().contains(player.getUniqueId().toString()))
			plugin.showMessage(player, plugin.getMessage("add"));
	}
}
