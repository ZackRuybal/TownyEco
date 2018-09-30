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
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.lang.enums.LanguageNodes;
import de.articdive.townyeco.objects.abstractions.TEShop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageWorker {
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final boolean entranceMSGEnabled = main.getMainConfig().getBoolean(ConfigYMLNodes.MESSAGES_SHOPS_ENTERANCE_MESSAGE_ENABLED);
	private static final boolean exitMSGEnabled = main.getMainConfig().getBoolean(ConfigYMLNodes.MESSAGES_SHOPS_EXIT_MESSAGE_ENABLED);

	public static void sendShopEnterMessage(Player player, TEShop shop) {
		if (entranceMSGEnabled) {
			Language language = HibernateDatabase.getLanguageByPlayer(player);
			player.sendMessage(
					MessageHelper.finalizeMessage(
							PlaceHolderHelper.replacePlaceHolders(shop.getEntranceMessage(), language, shop)
					)
			);
		}
	}

	public static void sendShopExitMessage(Player player, TEShop shop) {
		Language language = HibernateDatabase.getLanguageByPlayer(player);
		if (exitMSGEnabled) {
			player.sendMessage(
					MessageHelper.finalizeMessage(
							PlaceHolderHelper.replacePlaceHolders(shop.getExitMessage(), language, shop)
					)
			);
		}
	}

	public static void sendMainCommandMenuMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getStringArray(LanguageNodes.COMMANDS_TOWNYECO_MENU, language), language)
				)
		);
	}

	public static void sendMainCommandInfoMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getStringArray(LanguageNodes.COMMANDS_TOWNYECO_INFO, language), language)
				)
		);
	}

	public static void sendMainCommandOptionsMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getStringArray(LanguageNodes.COMMANDS_TOWNYECO_OPTIONS, language), language)
				)
		);
	}

	public static void sendMainCommandVersionMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getString(LanguageNodes.COMMANDS_TOWNYECO_VERSION, language), language)
				)
		);
	}

	public static void sendMainCommandOptionsLanguageMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getString(LanguageNodes.COMMANDS_TOWNYECO_OPTIONS_LANGUAGE, language), language)
				)
		);
	}

	public static void sendMainCommandOptionsLanguageInvalidMessage(CommandSender sender, String input) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getString(LanguageNodes.COMMANDS_TOWNYECO_OPTIONS_LANGUAGE_INVALID, language), language, input)
				)
		);
	}

	public static void sendMainCommandOptionsLanguageSuccessfulMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getString(LanguageNodes.COMMANDS_TOWNYECO_OPTIONS_LANGUAGE_SUCCESSFUL, language), language)
				)
		);
	}

	public static void sendConsoleNotAvailableMessage(CommandSender sender) {
		Language language = LanguageHandler.getPluginLanguage();
		if (sender instanceof Player) {
			language = HibernateDatabase.getLanguageByPlayer((Player) sender);
		}
		sender.sendMessage(
				MessageHelper.finalizeMessage(
						PlaceHolderHelper.replacePlaceHolders(LanguageHandler.getString(LanguageNodes.COMMANDS_CONSOLE_NOT_AVAILABLE, language), language)
				)
		);
	}
}
