// Created by Lukas Mansour on the 2018-09-08 at 18:54:47
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco;

import de.articdive.commentedconfiguration.CommentedConfiguration;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.listeners.PlayerConnectionListener;
import de.articdive.townyeco.listeners.ServerListener;
import de.articdive.townyeco.listeners.ServerWorldListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public final class TownyEco extends JavaPlugin {

	private String version;
	private final CommentedConfiguration mainConfig = new CommentedConfiguration(ConfigYMLNodes.class, getDataFolder().getPath() + File.separator + "config.yml");
	private final ArrayList<Listener> listeners = new ArrayList<>();

	@Override
	public void onEnable() {
		// Update version.
		version = this.getDescription().getVersion();
		// Update Configuration Version.
		mainConfig.setNode(ConfigYMLNodes.VERSION, version);
		// Initialize Static classes.
		LanguageHandler.initialize();
		HibernateDatabase.initialize();
		// Register Events
		registerEvents();
		// Register Commands
		registerCommands();
	}

	@Override
	public void onDisable() {
		// Unload Hibernate
		HibernateDatabase.close();
	}

	private void registerEvents() {
		listeners.add(new ServerListener());
		listeners.add(new ServerWorldListener());
		listeners.add(new PlayerConnectionListener());
	}


	private void registerCommands() {
		// TODO: Add Commands
	}

	/**
	 * Get TownyEco's version.
	 *
	 * @return - TownyEco's version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Get TownyEco's main CommentedConfiguration.
	 *
	 * @return - TownyEco's Main Commented Configuration.
	 */
	public CommentedConfiguration getMainConfig() {
		return mainConfig;
	}

	/**
	 * Get main (Root) folder of TownyEco.
	 *
	 * @return - TownyEco's root folder
	 */
	public String getRootFolder() {
		return this.getDataFolder().getPath();
	}

	/**
	 * Get the folder for DBs.
	 *
	 * @return - Database folder
	 */
	public String getDatabaseFolder() {
		return getRootFolder() + File.separator + "data";
	}

	/**
	 * Get the folder for languages.
	 *
	 * @return - Language folder.
	 */
	public String getLanguageFolder() {
		return getRootFolder() + File.separator + "lang";
	}

	/**
	 * Get the database changelog folder.
	 *
	 * @return - Database changelog folder
	 */
	public String getDatabaseChangelogFolder() {
		return getDatabaseFolder() + File.separator + "db-changelogs";
	}

	/**
	 * Get all the listeners registered by TownyEco.
	 *
	 * @return - List of all listeners.
	 */
	public ArrayList<Listener> getListeners() {
		return listeners;
	}
}
