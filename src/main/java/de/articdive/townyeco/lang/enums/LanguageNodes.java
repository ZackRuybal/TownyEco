package de.articdive.townyeco.lang.enums;


/*
 * Created by Lukas Mansour on the 8/2/18 1:11 PM.
 * This work is licensed under the "Creative Commons Attribution-NonCommercial-NoDerivative International License". (Short Code: CC BY-NC-ND 4.0 )
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

public enum LanguageNodes {
	// Logging
	LOGGING_DATABASE_FAILED_DB_CONNECTION("logging.database.failed_db_connection"),
	LOGGING_DATABASE_DB_CONVERSION("logging.database.db_conversion"),
	LOGGING_DATABASE_FAILED_TO_SAVE_OBJECT("logging.database.failed_to_save_object"),
	LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT("logging.database.failed_to_load_object"),
	LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT("logging.database.failed_to_delete_object"),
	LOGGING_DATABASE_DB_CONVERSION_FAILED("logging.database.db_conversion_failed");

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
