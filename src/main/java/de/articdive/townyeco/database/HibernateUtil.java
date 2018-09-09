// Created by Lukas Mansour on the 2018-09-08 at 18:55:15
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.database;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.LanguageNodes;
import de.articdive.townyeco.objects.TEPlayer;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import javax.persistence.Entity;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HibernateUtil {
	private static TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final Logger logger = LogManager.getLogger("de.articdive.metropoles.database");

	public static SessionFactory load() {
		// Get Login Information for LOAD:
		String loaddbtype = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_TYPE);
		String loaddbhost = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_HOSTNAME);
		String loaddbport = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_PORT);
		String loaddbschema = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_SCHEMA_NAME);
		String loaddbuser = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_USERNAME);
		String loaddbpass = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_LOAD_PASSWORD);
		// Get Login Information for SAVE:
		String savedbtype = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_TYPE);
		String savedbhost = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_HOSTNAME);
		String savedbport = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_PORT);
		String savedbschema = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_SCHEMA_NAME);
		String savedbpass = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_PASSWORD);
		String savedbuser = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_USERNAME);

		String loadcompare = loaddbtype + loaddbhost + loaddbport + loaddbschema;
		String savecompare = savedbtype + savedbhost + savedbport + savedbschema;

		// Create Registiry:
		StandardServiceRegistry standardServiceRegistry = getRegistry(savedbtype, savedbhost, savedbport, savedbschema, savedbuser, savedbpass);
		// Make sources.
		MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);

		// Get all @Entity classes quickly.
		new FastClasspathScanner().addClassLoader(HibernateDatabase.class.getClassLoader()).matchClassesWithAnnotation(Entity.class, metadataSources::addAnnotatedClass).scan();

		try {
			// Liquibase
			LiquibaseSchemaGenerator.liquify(metadataSources.getServiceRegistry().getService(ConnectionProvider.class).getConnection());
			Metadata metadata = metadataSources.buildMetadata();
			SessionFactory saveSessionFactory = metadata.buildSessionFactory();
			if (!loadcompare.equals(savecompare)) {
				// Save DB is different to load DB
				main.getLogger().info(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_DB_CONVERSION, LanguageHandler.getPluginLanguage()));
				logger.log(Level.INFO, "Converting data...");
				// Create Registry:
				StandardServiceRegistry loadregistry = getRegistry(loaddbtype, loaddbhost, loaddbport, loaddbschema, loaddbuser, loaddbpass);
				convertDatabase(loadregistry, saveSessionFactory);
			}
			return saveSessionFactory;
		} catch (Exception e) {
			// Any exceptions Handled:
			StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
			logger.log(Level.FATAL, e);
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_DB_CONNECTION, LanguageHandler.getPluginLanguage()));
			return null;
		}
	}

	private static void convertDatabase(StandardServiceRegistry loadregistry, SessionFactory saveSessionFactory) {
		// Make Sources
		MetadataSources loadsources = new MetadataSources(loadregistry);
		// Get all @Entity classes quickly.
		new FastClasspathScanner().addClassLoader(HibernateDatabase.class.getClassLoader()).matchClassesWithAnnotation(Entity.class, loadsources::addAnnotatedClass).scan();
		try {
			LiquibaseSchemaGenerator.liquify(loadsources.getServiceRegistry().getService(ConnectionProvider.class).getConnection());

			Metadata loadmetadata = loadsources.buildMetadata();
			SessionFactory loadSessionFactory = loadmetadata.buildSessionFactory();

			List<TEPlayer> tePlayers = new ArrayList<>(getAllTEPlayers(loadSessionFactory));
			for (TEPlayer tePlayer : tePlayers) {
				saveObject(tePlayer, saveSessionFactory);
			}
			loadSessionFactory.close();
			loadregistry.close();
			StandardServiceRegistryBuilder.destroy(loadregistry);
			logger.log(Level.INFO, "Changing values in the configuration");
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_TYPE, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_TYPE));
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_USERNAME, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_USERNAME));
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_PASSWORD, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_PASSWORD));
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_HOSTNAME, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_HOSTNAME));
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_PORT, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_PORT));
			main.getMainConfig().setNode(ConfigYMLNodes.DATABASE_LOAD_SCHEMA_NAME, main.getMainConfig().getString(ConfigYMLNodes.DATABASE_SAVE_SCHEMA_NAME));
		} catch (Exception e) {
			StandardServiceRegistryBuilder.destroy(loadregistry);
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_DB_CONVERSION_FAILED, LanguageHandler.getPluginLanguage()));
			logger.log(Level.FATAL, e);
		}
	}

	private static StandardServiceRegistry getRegistry(String dbtype, String hostname, String port, String
			schema, String username, String password) {
		StandardServiceRegistry registry = null;
		new File(main.getDatabaseFolder()).mkdirs();
		if (dbtype.equalsIgnoreCase("mysql")) {
			registry = new StandardServiceRegistryBuilder()
					.applySetting("hibernate.connection.username", username)
					.applySetting("hibernate.connection.password", password)
					.applySetting("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
					.applySetting("hibernate.connection.url", "jdbc:mysql://" + hostname + ":" + port + "/" + schema + "?verifyServerCertificate=false&useSSL=false&useUnicode=true&characterEncoding=utf-8")
					.applySetting("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
					.applySetting("connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
					.applySetting("hibernate.hikari.connectionTimeout", "20000")
					.applySetting("hibernate.hikari.minimumIdle", "10")
					.applySetting("hibernate.hikari.maximumPoolSize", "20")
					.applySetting("hibernate.hikari.idleTimeout", "300000")
					.applySetting("hibernate.enable_lazy_load_no_trans", true)
					.applySetting("hibernate.show_sql", false)
					.applySetting("hibernate.format_sql", false)
					.applySetting("hibernate.physical_naming_strategy", "de.articdive.metropoles.database.MetropolesHibernateNamingStrategy")
					.build();
		}
		if (dbtype.equalsIgnoreCase("h2") || registry == null) {
			registry = new StandardServiceRegistryBuilder()
					.applySetting("hibernate.connection.username", "")
					.applySetting("hibernate.connection.password", "")
					.applySetting("hibernate.connection.driver_class", "org.h2.Driver")
					.applySetting("hibernate.connection.url", "jdbc:h2:./" + main.getDatabaseFolder() + File.separator + schema + ";MV_STORE=FALSE;AUTO_SERVER=true;")
					.applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
					.applySetting("connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
					.applySetting("hibernate.hikari.connectionTimeout", "20000")
					.applySetting("hibernate.hikari.minimumIdle", "10")
					.applySetting("hibernate.hikari.maximumPoolSize", "20")
					.applySetting("hibernate.hikari.idleTimeout", "300000")
					.applySetting("hibernate.enable_lazy_load_no_trans", true)
					.applySetting("hibernate.show_sql", false)
					.applySetting("hibernate.format_sql", false)
					.applySetting("hibernate.physical_naming_strategy", "de.articdive.metropoles.database.MetropolesHibernateNamingStrategy")
					.build();
		}
		return registry;
	}

	private static List<TEPlayer> getAllTEPlayers(SessionFactory sessionFactory) {
		logger.log(Level.INFO, "Getting all TEPlayers");
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEPlayer> criteriaQuery = criteriaBuilder.createQuery(TEPlayer.class);
			Root<TEPlayer> root = criteriaQuery.from(TEPlayer.class);
			criteriaQuery.select(root);
			TypedQuery<TEPlayer> query = s.createQuery(criteriaQuery);
			List<TEPlayer> list = query.getResultList();
			tx.commit();
			return list;
		} catch (NoResultException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all metropolitains").replace("{criteria}", ""));
			return new ArrayList<>();
		} finally {
			s.close();
		}
	}

	private static void saveObject(TownyEcoObject object, SessionFactory sessionFactory) {
		logger.log(Level.INFO, "Saving Object: " + object.getType());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			s.saveOrUpdate(object);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_SAVE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{objects}", object.getType()));
		} finally {
			s.close();
		}
	}
}
