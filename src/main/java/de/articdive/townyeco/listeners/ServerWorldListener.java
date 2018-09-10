// Created by Lukas Mansour on the 2018-09-09 at 10:14:37
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
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.List;

public class ServerWorldListener implements Listener {
	static List<World> worlds = new ArrayList<>();

	public ServerWorldListener() {
		Bukkit.getPluginManager().registerEvents(this, TownyEco.getPlugin(TownyEco.class));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldLoadEvent event) {
		worlds.add(event.getWorld());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldUnload(WorldUnloadEvent event) {
		World world = event.getWorld();
		TEWorld teWorld = HibernateDatabase.getTEWorld(world.getUID());
		if (teWorld != null) {
			// TODO: Update Attributes.
			teWorld.save();
		}
	}
}
