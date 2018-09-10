// Created by Lukas Mansour on the 2018-09-10 at 15:48:53
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.listeners;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.objects.TEWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerListener implements Listener {

	public ServerListener() {
		Bukkit.getPluginManager().registerEvents(this, TownyEco.getPlugin(TownyEco.class));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onServerLoadFinish(ServerLoadEvent event) {
		// Set SessionFactory
		HibernateDatabase.load();
		// Handle Worlds
		handleWorlds();
	}

	private void handleWorlds() {
		for (World world : ServerWorldListener.worlds) {
			TEWorld teWorld = HibernateDatabase.getTEWorld(world.getUID());
			if (teWorld != null) {
				teWorld.setName(world.getName());
			} else {
				teWorld = new TEWorld(world.getUID());
				teWorld.setName(world.getName());
			}
			teWorld.save();
		}
		ServerWorldListener.worlds = null;
	}
}
