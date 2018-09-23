// Created by Lukas Mansour on the 2018-09-11 at 19:45:57
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.listeners;

import com.palmergames.bukkit.towny.event.PlotChangeTypeEvent;
import de.articdive.townyeco.listeners.abstractions.TownyEcoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class TownyListener extends TownyEcoListener {

	public TownyListener() {super();}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onTownyCreateShopPlot(PlotChangeTypeEvent event) {

	}
}
