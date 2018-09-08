package de.articdive.townyeco.listeners;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.objects.TEPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {
	private static TownyEco main = TownyEco.getPlugin(TownyEco.class);

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent event) {
		if (event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) {
			Player player = event.getPlayer();
			TEPlayer tePlayer = HibernateDatabase.getTEPlayer(player.getUniqueId());
			if (tePlayer != null) {
				tePlayer.setLastKnownName(player.getName());
			} else {
				tePlayer = new TEPlayer(player.getUniqueId());
				tePlayer.setLastKnownName(player.getName());
			}
			tePlayer.save();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		TEPlayer tePlayer = HibernateDatabase.getTEPlayer(player.getUniqueId());
		if (tePlayer != null) {
			tePlayer.save();
		}
	}
}
