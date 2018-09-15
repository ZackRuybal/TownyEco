// Created by Lukas Mansour on the 2018-09-14 at 14:56:56
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import org.bukkit.ChatColor;

public class MessageHelper {
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final boolean prefixed = main.getMainConfig().getBoolean(ConfigYMLNodes.MESSAGES_PREFIX_ENABLED);
	private static final String prefix = main.getMainConfig().getString(ConfigYMLNodes.MESSAGES_PREFIX);


	public static String finalizeMessage(String message) {
		if (prefixed) {
			return ChatColor.translateAlternateColorCodes('&', prefix + message);
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
