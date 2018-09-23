// Created by Lukas Mansour on the 2018-09-13 at 18:27:15
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.listeners;

import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.listeners.abstractions.TownyEcoListener;
import de.articdive.townyeco.objects.TEShop;
import de.articdive.townyeco.workers.ShopMessageWorker;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener extends TownyEcoListener {

	public PlayerMovementListener() {super();}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!event.isCancelled()) {
			Location from = event.getFrom();
			Location to = event.getTo();
			// Shops
			TEShop fromShop = HibernateDatabase.getTEServerShopByLocation(from.getWorld().getUID(), from.getBlockX(), from.getBlockY(), from.getBlockZ());
			if (TownyEco.townyEnabled) {
				if (fromShop == null) {
					TownBlock townBlock = TownyUniverse.getTownBlock(from);
					if (townBlock != null) {
						fromShop = HibernateDatabase.getTETownyShopByLocation(from.getWorld().getUID(), townBlock.getX(), townBlock.getZ());
					}
				}
			}
			TEShop toShop = HibernateDatabase.getTEServerShopByLocation(to.getWorld().getUID(), to.getBlockX(), to.getBlockY(), to.getBlockZ());
			if (TownyEco.townyEnabled) {
				if (toShop == null) {
					TownBlock townBlock = TownyUniverse.getTownBlock(to);
					if (townBlock != null) {
						toShop = HibernateDatabase.getTETownyShopByLocation(to.getWorld().getUID(), townBlock.getX(), townBlock.getZ());
					}
				}
			}
			if (fromShop == null) {
				if (toShop != null) {
					ShopMessageWorker.sendEnterMessage(event.getPlayer(), toShop);
				}
			}
			if (toShop == null) {
				if (fromShop != null) {
					ShopMessageWorker.sendExitMessage(event.getPlayer(), fromShop);
				}
			}
		}
	}
}
