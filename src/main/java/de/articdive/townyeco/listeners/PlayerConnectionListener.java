// Created by Lukas Mansour on the 2018-09-08 at 18:52:46
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
// Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.listeners;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.objects.TEPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

	public PlayerConnectionListener() {
		Bukkit.getPluginManager().registerEvents(this, TownyEco.getPlugin(TownyEco.class));
	}

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
			// TODO: Update Attributes.
			tePlayer.save();
		}
	}
}
