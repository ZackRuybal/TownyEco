// Created by Lukas Mansour on the 2018-09-13 at 18:29:55
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.listeners.abstractions;

import de.articdive.townyeco.TownyEco;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class TownyEcoListener implements Listener {

	public TownyEcoListener() {
		Bukkit.getPluginManager().registerEvents(this, TownyEco.getPlugin(TownyEco.class));
	}
}
