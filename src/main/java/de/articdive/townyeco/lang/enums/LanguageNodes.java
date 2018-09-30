// Created by Lukas Mansour on the 2018-09-08 at 18:55:29
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.lang.enums;

public enum LanguageNodes {
	// Logging
	LOGGING_DATABASE_FAILED_DB_CONNECTION("logging.database.failed_db_connection"),
	LOGGING_DATABASE_DB_CONVERSION("logging.database.db_conversion"),
	LOGGING_DATABASE_FAILED_TO_SAVE_OBJECT("logging.database.failed_to_save_object"),
	LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT("logging.database.failed_to_load_object"),
	LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT("logging.database.failed_to_delete_object"),
	LOGGING_DATABASE_DB_CONVERSION_FAILED("logging.database.db_conversion_failed"),
	// Messages
	PLACEHOLDERS_TIMEFORMAT("placeholders.timeformat"),
	PLACEHOLDERS_VERSION("placeholders.version"),
	MESSAGES_SHOPS_DEFAULT_ENTRANCE("messages.shops.default_enterance"),
	MESSAGES_SHOPS_DEFAULT_EXIT("messages.shops.default_exit"),
	COMMANDS_TOWNYECO_MENU("commands.townyeco.main.menu"),
	COMMANDS_TOWNYECO_INFO("commands.townyeco.main.info"),
	COMMANDS_TOWNYECO_OPTIONS("commands.townyeco.main.options"),
	COMMANDS_TOWNYECO_VERSION("commands.townyeco.main.version"),
	COMMANDS_TOWNYECO_OPTIONS_LANGUAGE("commands.townyeco.main.options_language");

	private final String node;

	LanguageNodes(String node) {

		this.node = node;
	}

	/**
	 * Retrieves the root for a language string.
	 *
	 * @return The root for a language string.
	 */
	public String getNode() {

		return node;
	}
}
