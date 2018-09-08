package de.articdive.townyeco;

import de.articdive.commentedconfiguration.CommentedConfiguration;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.database.HibernateDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TownyEco extends JavaPlugin {

	private String version;
	private CommentedConfiguration mainConfig = new CommentedConfiguration(ConfigYMLNodes.class, getDataFolder().getPath() + System.getProperty("file.separator") + "config.yml");

	public TownyEco() {
		// Update version.
		version = this.getDescription().getVersion();
		// Update Configuration Version.
		mainConfig.setNode(ConfigYMLNodes.VERSION, version);
	}

	@Override
	public void onLoad() {
		// Initialize Static classes.
		HibernateDatabase.initialize();
	}

	@Override
	public void onEnable() {
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch (ClassNotFoundException e) {
			getLogger().severe("Disabling Metropoles: You're not running Spigot...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		// Register Events
		registerEvents();
		// Register Commands
		registerCommands();
		// Set SessionFactory
		HibernateDatabase.load();

	}

	@Override
	public void onDisable() {
		// Unload Hibernate
		HibernateDatabase.close();
	}

	private void registerEvents() {
		// TODO: Add Events
	}


	private void registerCommands() {
		// TODO: Add Commands
	}

	/**
	 * Get Metropoles' version.
	 *
	 * @return - Metropoles' version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Get Metropoles' main CommentedConfiguration.
	 *
	 * @return - Metropoles' Main Commented Configuration.
	 */
	public CommentedConfiguration getMainConfig() {
		return mainConfig;
	}

	/**
	 * Get main (Root) folder of Metropoles.
	 *
	 * @return - Metropoles' root folder
	 */
	public String getRootFolder() {
		return this.getDataFolder().getPath();
	}

	/**
	 * Get the folder for DBs
	 *
	 * @return - Database folder
	 */
	public String getDatabaseFolder() {
		return getRootFolder() + System.getProperty("file.separator") + "data";
	}

	/**
	 * Get the folder for languages.
	 *
	 * @return - Language folder.
	 */
	public String getLanguageFolder() {
		return getRootFolder() + System.getProperty("file.separator") + "lang";
	}

	/**
	 * Get the database changelog folder
	 *
	 * @return - Database changelog folder
	 */
	public String getDatabaseChangelogFolder() {
		return getDatabaseFolder() + System.getProperty("file.separator") + "db-changelogs";
	}
}
