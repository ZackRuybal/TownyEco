// Created by Lukas Mansour on the 2018-09-13 at 20:39:37
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.workers;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.helpers.MessageHelper;
import de.articdive.townyeco.helpers.PlaceHolderHelper;
import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.objects.TEShop;
import org.bukkit.entity.Player;

public class ShopMessageWorker {
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final boolean entranceMSGEnabled = main.getMainConfig().getBoolean(ConfigYMLNodes.MESSAGES_SHOPS_ENTERANCE_MESSAGE_ENABLED);
	private static final boolean exitMSGEnabled = main.getMainConfig().getBoolean(ConfigYMLNodes.MESSAGES_SHOPS_EXIT_MESSAGE_ENABLED);

	public static void sendEnterMessage(Player player, TEShop shop) {
		if (entranceMSGEnabled) {
			Language language = HibernateDatabase.getLanguageByPlayer(player);
			player.sendMessage(
					MessageHelper.finalizeMessage(
							PlaceHolderHelper.replacePlaceHolders(shop.getEntranceMessage(), language, shop)
					)
			);
		}
	}

	public static void sendExitMessage(Player player, TEShop shop) {
		Language language = HibernateDatabase.getLanguageByPlayer(player);
		if (exitMSGEnabled) {
			player.sendMessage(
					MessageHelper.finalizeMessage(
							PlaceHolderHelper.replacePlaceHolders(shop.getExitMessage(), language, shop)
					)
			);
		}
	}
}
