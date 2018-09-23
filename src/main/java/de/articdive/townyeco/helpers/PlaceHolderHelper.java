// Created by Lukas Mansour on the 2018-09-15 at 08:01:03
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers;

import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.lang.enums.LanguageNodes;
import de.articdive.townyeco.objects.TECurrency;
import de.articdive.townyeco.objects.TEPlayer;
import de.articdive.townyeco.objects.TEServerShop;
import de.articdive.townyeco.objects.TEShop;
import de.articdive.townyeco.objects.TETownyShop;
import de.articdive.townyeco.objects.TEWorld;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceHolderHelper {
	public static String replacePlaceHolders(String message, Language language, TownyEcoObject... townyEcoObjects) {
		for (TownyEcoObject object : townyEcoObjects) {
			SimpleDateFormat sdf = new SimpleDateFormat(LanguageHandler.getString(LanguageNodes.MESSAGES_TIMEFORMAT, language));
			message = message.replace("{current-time}", sdf.format(new Date()));
			if (object instanceof TEPlayer) {
				message = message.replace("{player-uuid}", ((TEPlayer) object).getIdentifier().toString());
				message = message.replace("{player-name}", ((TEPlayer) object).getLastKnownName());
				message = message.replace("{player-registered}", sdf.format(((TEPlayer) object).getRegistered()));
				message = message.replace("{player-lastonline}", sdf.format(((TEPlayer) object).getLastOnline()));
				message = message.replace("{player-language}", ((TEPlayer) object).getLanguage().getName());
			}
			if (object instanceof TECurrency) {
				message = message.replace("{currency-name}", ((TECurrency) object).getName());
			}
			if (object instanceof TEWorld) {
				message = message.replace("{world-uuid}", ((TEWorld) object).getIdentifier().toString());
				message = message.replace("{world-name}", ((TEWorld) object).getName());
				message = message.replace("{world-maincurrency-name}", ((TEWorld) object).getMainCurrency().getName());
				// TODO: Lists
//				message.replace("{}", ((TEWorld) object).getCurrencies())
//				message.replace("{}", ((TEWorld) object).getShops())
			}
			if (object instanceof TEShop) {
				message = message.replace("{shop-uuid}", ((TEShop) object).getIdentifier().toString());
				message = message.replace("{shop-name}", ((TEShop) object).getName());
				if (object instanceof TEServerShop) {
				}
				if (object instanceof TETownyShop) {

				}
			}
		}
		return message;
	}
}
