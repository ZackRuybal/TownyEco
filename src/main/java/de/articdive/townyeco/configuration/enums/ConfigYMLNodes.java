// Created by Lukas Mansour on the 2018-09-08 at 18:55:03
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.configuration.enums;


import de.articdive.commentedconfiguration.interfaces.ConfigNodes;

@SuppressWarnings("unused")
public enum ConfigYMLNodes implements ConfigNodes {
	// Version:
	VERSION_HEADER("version", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version of Metropolis."),
	LAST_RUN_VERSION(
			"version.last_run_version",
			"",
			"# This is for updating language files. Please do not edit."),
	DATABASE_HEADER("database", "",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                Database Configuration                | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			"",
			"# If you want to change/reset your database change both sections.",
			"# If you want to convert your database you only need to change the save section."),
	DATABASE_TABLE_PREFIX("database.table_prefix", "TOWNYECO_", "# Table prefix for SQL tables"),
	// Database LOAD:
	DATABASE_LOAD_HEADER("database.load", ""),
	DATABASE_LOAD_TYPE("database.load.type", "h2", "# Load database type (options: h2, mysql)"),
	DATABASE_LOAD_HOSTNAME("database.load.hostname", "localhost", "# Load database hostname"),
	DATABASE_LOAD_PORT("database.load.port", "3306", "# Load database port"),
	DATABASE_LOAD_SCHEMA_NAME("database.load.dbname", "townyeco", "# Load database schema / database"),
	DATABASE_LOAD_USERNAME("database.load.username", "", "# Load database username"),
	DATABASE_LOAD_PASSWORD("database.load.password", "", "# Load database password"),
	// Database SAVE:
	DATABASE_SAVE_HEADER("database.save", ""),
	DATABASE_SAVE_TYPE("database.save.type", "h2", "# Save database type (options: h2, mysql)"),
	DATABASE_SAVE_HOSTNAME("database.save.hostname", "localhost", "# Save database hostname"),
	DATABASE_SAVE_PORT("database.save.port", "3306", "# Save database port"),
	DATABASE_SAVE_SCHEMA_NAME("database.save.dbname", "townyeco", "# Save database schema / database"),
	DATABASE_SAVE_USERNAME("database.save.username", "", "# Save database username"),
	DATABASE_SAVE_PASSWORD("database.save.password", "", "# Save database password"),
	LANGUAGE_HEADER("language", "",
			"",
			"############################################################",
			"# +------------------------------------------------------+ #",
			"# |                Language Configuration                | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			"",
			"# Valid languages: 'english' and 'german'"),
	PLAYER_LANGUAGES("language.player_languages", "english|german",
			"# The languages players can set the plugin to be in for themselves.",
			"# The default player language comes first."),
	PLUGIN_LANGUAGE("language.plugin_language", "english", "# The language the plugin logs in."),
	LOGGING_HEADER("logging", "",
			"",
			"###########################################################",
			"# +-----------------------------------------------------+ #",
			"# |                Logging Configuration                | #",
			"# +-----------------------------------------------------+ #",
			"###########################################################",
			"",
			"# This section configures TownyEco's logging system"),
	LOGGING_FILE_ENABLED("logging.file_enabled", "true", "# Should TownyEco log to files?");


	private final String node;
	private final String defaultValue;
	private String[] comments;

	ConfigYMLNodes(String node, String defaultValue, String... comments) {

		this.node = node;
		this.defaultValue = defaultValue;
		this.comments = comments;
	}

	public String getNode() {

		return node;
	}

	public String getDefaultValue() {

		return defaultValue;
	}

	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}
}
