// Created by Lukas Mansour on the 2018-09-08 at 18:55:12
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.database;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.lang.enums.LanguageNodes;
import de.articdive.townyeco.objects.TECurrency;
import de.articdive.townyeco.objects.TENPC;
import de.articdive.townyeco.objects.TEPlayer;
import de.articdive.townyeco.objects.TEServerShop;
import de.articdive.townyeco.objects.TEShop;
import de.articdive.townyeco.objects.TETownyShop;
import de.articdive.townyeco.objects.TEWorld;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.entity.Player;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HibernateDatabase {
	private static SessionFactory sessionFactory;
	private static final Logger logger = LogManager.getLogger("de.articdive.townyeco.database");
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static CompletableFuture<SessionFactory> result = new CompletableFuture<>();

	public static void initialize() {
		// Log4j2
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();

		List<Appender> appenders = new ArrayList<>();
		List<AppenderRef[]> appendersreferences = new ArrayList<>();

		Layout<String> layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, null, config, null, null, false, false, null, null);

		appenders.add(FileAppender.createAppender(main.getRootFolder() + "/logs/Hibernate.log", "false", "false", "FileHiberate", "true", "false", "false", "0", layout, null, "false", null, config));
		appenders.add(FileAppender.createAppender(main.getRootFolder() + "/logs/Liquibase.log", "false", "false", "FileLiquibase", "true", "false", "false", "0", layout, null, "false", null, config));
		appenders.add(FileAppender.createAppender(main.getRootFolder() + "/logs/HikariCP.log", "false", "false", "FileHikariCP", "true", "false", "false", "0", layout, null, "false", null, config));
		appenders.add(FileAppender.createAppender(main.getRootFolder() + "/logs/TownyEco.log", "false", "false", "FileTownyEco", "true", "false", "false", "0", layout, null, "false", null, config));

		for (Appender appender : appenders) {
			appender.start();
			config.addAppender(appender);
			appendersreferences.add(new AppenderRef[]{AppenderRef.createAppenderRef(appender.getName(), Level.ALL, null)});
		}

		LoggerConfig hibernateLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "org.hibernate", "false", appendersreferences.get(0), null, config, null);
		hibernateLoggerConfig.addAppender(appenders.get(0), Level.ALL, null);
		LoggerConfig liquibaseLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "liquibase", "false", appendersreferences.get(1), null, config, null);
		liquibaseLoggerConfig.addAppender(appenders.get(1), Level.ALL, null);
		LoggerConfig hikariCPLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "com.zaxxer.hikari", "false", appendersreferences.get(2), null, config, null);
		hikariCPLoggerConfig.addAppender(appenders.get(2), Level.ALL, null);
		LoggerConfig townyecoLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "de.articdive.townyeco.database", "false", appendersreferences.get(3), null, config, null);
		townyecoLoggerConfig.addAppender(appenders.get(3), Level.ALL, null);
		if (!(main.getMainConfig().getBoolean(ConfigYMLNodes.LOGGING_FILE_ENABLED))) {
			hibernateLoggerConfig.setLevel(Level.OFF);
			liquibaseLoggerConfig.setLevel(Level.OFF);
			hikariCPLoggerConfig.setLevel(Level.OFF);
			townyecoLoggerConfig.setLevel(Level.OFF);
		}
		config.addLogger("org.hibernate", hibernateLoggerConfig);
		config.addLogger("liquibase", liquibaseLoggerConfig);
		config.addLogger("com.zaxxer.hikari", hikariCPLoggerConfig);
		config.addLogger("de.articdive.townyeco.database", townyecoLoggerConfig);
		ctx.updateLoggers();

		Executor executor = Executors.newScheduledThreadPool(1);
		result = CompletableFuture.supplyAsync(HibernateUtil::load, executor);
	}

	public static void load() {
		try {
			sessionFactory = result.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}


	public static void close() {
		logger.log(Level.ALL, "Closing SessionFactory");
		sessionFactory.close();
	}


	public static void saveObject(TownyEcoObject object) {
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

	// GET ALL METHODS
	public static List<TEPlayer> getAllTEPlayers() {
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
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all TEPlayers").replace("{criteria}", "*"));
			return new ArrayList<>();
		} finally {
			s.close();
		}
	}

	public static List<TEWorld> getAllTEWorlds() {
		logger.log(Level.INFO, "Getting all TEWorlds");
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEWorld> criteriaQuery = criteriaBuilder.createQuery(TEWorld.class);
			Root<TEWorld> root = criteriaQuery.from(TEWorld.class);
			criteriaQuery.select(root);
			TypedQuery<TEWorld> query = s.createQuery(criteriaQuery);
			List<TEWorld> list = query.getResultList();
			tx.commit();
			return list;
		} catch (NoResultException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all TEWorlds").replace("{criteria}", "*"));
			return new ArrayList<>();
		} finally {
			s.close();
		}
	}

	public static List<TECurrency> getAllTECurrencies() {
		logger.log(Level.INFO, "Getting all TECurrencies");
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TECurrency> criteriaQuery = criteriaBuilder.createQuery(TECurrency.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);
			criteriaQuery.select(root);
			TypedQuery<TECurrency> query = s.createQuery(criteriaQuery);
			List<TECurrency> list = query.getResultList();
			tx.commit();
			return list;
		} catch (NoResultException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all TEShops").replace("{criteria}", "*"));
			return new ArrayList<>();
		} finally {
			s.close();
		}
	}

	public static List<TEShop> getAllTEShops() {
		logger.log(Level.INFO, "Getting all TEShops");
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEShop> criteriaQuery = criteriaBuilder.createQuery(TEShop.class);
			Root<TEShop> root = criteriaQuery.from(TEShop.class);
			criteriaQuery.select(root);
			TypedQuery<TEShop> query = s.createQuery(criteriaQuery);
			List<TEShop> list = query.getResultList();
			tx.commit();
			return list;
		} catch (NoResultException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all TEShops").replace("{criteria}", "*"));
			return new ArrayList<>();
		} finally {
			s.close();
		}
	}


	// GET METHODS
	public static TEPlayer getTEPlayer(UUID identifier) {
		logger.log(Level.INFO, "Getting TEPlayer by identifier: " + identifier.toString());
		TEPlayer tePlayer = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			tePlayer = s.get(TEPlayer.class, identifier);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEPlayer").replace("{criteria}", identifier.toString()));
		} finally {
			s.close();
		}
		return tePlayer;
	}

	public static List<TEPlayer> getTEPlayer(String lastKnownName) {
		logger.log(Level.INFO, "Getting TEPlayer(s) by lastKnownName: " + lastKnownName);
		List<TEPlayer> tePlayers = new ArrayList<>();
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEPlayer> criteriaQuery = criteriaBuilder.createQuery(TEPlayer.class);
			Root<TEPlayer> root = criteriaQuery.from(TEPlayer.class);
			criteriaQuery.select(root)
					.where(criteriaBuilder.equal(root.get("lastKnownName"), lastKnownName));
			TypedQuery<TEPlayer> query = s.createQuery(criteriaQuery);
			tePlayers = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEPlayer").replace("{criteria}", lastKnownName));
		} finally {
			s.close();
		}
		return tePlayers;
	}

	public static TENPC getTENPC(String name) {
		logger.log(Level.INFO, "Getting TENPC by name: " + name);
		TENPC teNPC = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TENPC> criteriaQuery = criteriaBuilder.createQuery(TENPC.class);
			Root<TENPC> root = criteriaQuery.from(TENPC.class);
			criteriaQuery.select(root)
					.where(criteriaBuilder.equal(root.get("name"), name));
			Query<TENPC> query = s.createQuery(criteriaQuery);
			teNPC = query.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TENPC").replace("{criteria}", name));
		} finally {
			s.close();
		}
		return teNPC;
	}

	public static TEWorld getTEWorld(UUID identifier) {
		logger.log(Level.INFO, "Getting TEWorld by identifier: " + identifier.toString());
		TEWorld teWorld = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			teWorld = s.get(TEWorld.class, identifier);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEWorld").replace("{criteria}", identifier.toString()));
		} finally {
			s.close();
		}
		return teWorld;
	}

	public static TEWorld getTEWorld(String name) {
		logger.log(Level.INFO, "Getting TEWorld(s) by name: " + name);
		TEWorld teWorld = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEWorld> criteriaQuery = criteriaBuilder.createQuery(TEWorld.class);
			Root<TEWorld> root = criteriaQuery.from(TEWorld.class);
			criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
			Query<TEWorld> query = s.createQuery(criteriaQuery);
			teWorld = query.uniqueResult();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEWorld").replace("{criteria}", name));
		} finally {
			s.close();
		}
		return teWorld;
	}

	public static List<TECurrency> getTECurrency(String name) {
		logger.log(Level.INFO, "Getting TECurrency(ies) by name: " + name);
		List<TECurrency> teCurrencies = new ArrayList<>();
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TECurrency> criteriaQuery = criteriaBuilder.createQuery(TECurrency.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);
			criteriaQuery.select(root)
					.where(criteriaBuilder.equal(root.get("name"), name));
			TypedQuery<TECurrency> query = s.createQuery(criteriaQuery);
			teCurrencies = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TECurrency").replace("{criteria}", name));
		} finally {
			s.close();
		}
		return teCurrencies;
	}

	public static List<TECurrency> getTECurrency(String name, String world) {
		logger.log(Level.INFO, "Getting TECurrency(ies) by name: " + name + " and by world:" + world);
		List<TECurrency> teCurrencies = new ArrayList<>();
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		TEWorld teWorld = getTEWorld(world);
		if (teWorld == null) {
			return new ArrayList<>();
		}
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TECurrency> criteriaQuery = criteriaBuilder.createQuery(TECurrency.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);
			criteriaQuery.select(root)
					.where(
							criteriaBuilder.equal(root.get("name"), name),
							criteriaBuilder.equal(root.get("world").get("identifier"),  teWorld.getIdentifier())
					);
			TypedQuery<TECurrency> query = s.createQuery(criteriaQuery);
			teCurrencies = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TECurrency").replace("{criteria}", name));
		} finally {
			s.close();
		}
		return teCurrencies;
	}

	public static TEShop getTEShop(UUID identifier) {
		logger.log(Level.INFO, "Getting TEShop by identifier: " + identifier.toString());
		TEShop teShop = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			teShop = s.get(TEShop.class, identifier);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEShop").replace("{criteria}", identifier.toString()));
		} finally {
			s.close();
		}
		return teShop;
	}

	public static TEServerShop getTEServerShopByLocation(UUID worldIdentifier, int x, int y, int z) {
		logger.log(Level.INFO, "Getting server shop by location: " + worldIdentifier.toString() + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z));
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		TEServerShop shop;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEServerShop> criteriaQuery = criteriaBuilder.createQuery(TEServerShop.class);

			Root<TEServerShop> root = criteriaQuery.from(TEServerShop.class);
			criteriaQuery.select(root)
					.where(
							criteriaBuilder.greaterThan(root.get("minX"), x),
							criteriaBuilder.greaterThan(root.get("minY"), y),
							criteriaBuilder.greaterThan(root.get("minZ"), z),
							criteriaBuilder.lessThan(root.get("maxX"), x),
							criteriaBuilder.lessThan(root.get("maxY"), y),
							criteriaBuilder.lessThan(root.get("maxZ"), z),
							criteriaBuilder.equal(root.get("world").get("identifier"),  worldIdentifier)
					);
			Query<TEServerShop> query = s.createQuery(criteriaQuery);
			shop = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEServerShop").replace("{criteria}", worldIdentifier.toString() + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + Integer.toString(z)));
			throw e;
		} finally {
			s.close();
		}
		return shop;
	}

	public static TETownyShop getTETownyShopByLocation(UUID worldIdentifier, int x, int z) {
		logger.log(Level.INFO, "Getting towny shop by coordinates: " + Integer.toString(x) + "," + Integer.toString(z) + " in world with identifier: " + worldIdentifier.toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		TETownyShop shop;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TETownyShop> criteriaQuery = criteriaBuilder.createQuery(TETownyShop.class);

			Root<TETownyShop> root = criteriaQuery.from(TETownyShop.class);
			criteriaQuery.select(root)
					.where(
							criteriaBuilder.equal(root.get("x"), x),
							criteriaBuilder.equal(root.get("z"), z),
							criteriaBuilder.equal(root.get("world").get("identifier"),  worldIdentifier)
					);
			Query<TETownyShop> query = s.createQuery(criteriaQuery);
			shop = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TETownyShop").replace("{criteria}", worldIdentifier.toString() + "," + Integer.toString(x) + "," + Integer.toString(z)));
			throw e;
		} finally {
			s.close();
		}
		return shop;
	}

	// DELETE METHODS
	public static void deleteTEPlayer(TEPlayer tePlayer) {
		logger.log(Level.INFO, "Deleting TEPlayer by identifier: " + tePlayer.getIdentifier().toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TEPlayer.class, tePlayer.getIdentifier());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEPlayer"));
			throw e;
		} finally {
			s.close();
		}
	}

	public static void deleteTENPC(TENPC teNPC) {
		logger.log(Level.INFO, "Deleting TENPC by name: " + teNPC.getName());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TENPC.class, teNPC.getName());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TENPC"));
			throw e;
		} finally {
			s.close();
		}
	}

	public static void deleteTEWorld(TEWorld teWorld) {
		logger.log(Level.INFO, "Deleting TEWorld by identifier: " + teWorld.getIdentifier().toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TEWorld.class, teWorld.getIdentifier());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEWorld"));
			throw e;
		} finally {
			s.close();
		}
	}

	public static void deleteTECurrency(TECurrency teCurrency) {
		logger.log(Level.INFO, "Deleting TECurrency by identifier: " + teCurrency.getName());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TECurrency.class, teCurrency.getName());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TECurrency"));
			throw e;
		} finally {
			s.close();
		}
	}

	public static void deleteTEShop(TEShop teShop) {
		logger.log(Level.INFO, "Deleting TEShop by identifier: " + teShop.getIdentifier().toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TEShop.class, teShop.getIdentifier());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "TEShop"));
			throw e;
		} finally {
			s.close();
		}
	}

	// OTHER (SPECIALIZED) METHODS
	public static Language getLanguageByPlayer(Player player) {
		logger.log(Level.INFO, "Getting language of player by identifier: " + player.getUniqueId().toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Language lang;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Language> criteriaQuery = criteriaBuilder.createQuery(Language.class);

			Root<TEPlayer> root = criteriaQuery.from(TEPlayer.class);
			criteriaQuery.select(root.get("language"))
					.where(criteriaBuilder.equal(root.get("identifier"), player.getUniqueId()));

			Query<Language> query = s.createQuery(criteriaQuery);
			lang = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Language - TEPlayer").replace("{criteria}", player.getUniqueId().toString()));
			throw e;
		} finally {
			s.close();
		}
		return lang;
	}

	// EXISTANCE METHODS
	public static boolean checkExistanceTEPlayer(UUID identifier) {
		logger.log(Level.INFO, "Checking existance of player by identifier: " + identifier.toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Long amount;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<TEPlayer> root = criteriaQuery.from(TEPlayer.class);

			criteriaQuery.select(criteriaBuilder.count(root))
					.where(criteriaBuilder.equal(root.get("identifier"), identifier));

			Query<Long> query = s.createQuery(criteriaQuery);
			amount = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Existance - TEPlayer").replace("{criteria}", identifier.toString()));
			throw e;
		} finally {
			s.close();
		}
		return amount > 0;
	}

	public static boolean checkExistanceTENPC(String name) {
		logger.log(Level.INFO, "Checking existance of NPC by name: " + name);
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Long amount;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<TENPC> root = criteriaQuery.from(TENPC.class);

			criteriaQuery.select(criteriaBuilder.count(root))
					.where(criteriaBuilder.equal(root.get("name"), name));

			Query<Long> query = s.createQuery(criteriaQuery);
			amount = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Existance - TENPC").replace("{criteria}", name));
			throw e;
		} finally {
			s.close();
		}
		return amount > 0;
	}

	public static boolean checkExistanceTECurrency(UUID identifier) {
		logger.log(Level.INFO, "Checking existance of currency by identifier: " + identifier.toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Long amount;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);

			criteriaQuery.select(criteriaBuilder.count(root))
					.where(criteriaBuilder.equal(root.get("identifier"), identifier));

			Query<Long> query = s.createQuery(criteriaQuery);
			amount = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Existance - TECurrency").replace("{criteria}", identifier.toString()));
			throw e;
		} finally {
			s.close();
		}
		return amount > 0;
	}

	public static boolean checkExistanceTECurrency(String currencyName) {
		logger.log(Level.INFO, "Checking existance of currency by name: " + currencyName);
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Long amount;
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);

			criteriaQuery.select(criteriaBuilder.count(root))
					.where(criteriaBuilder.equal(root.get("name"), currencyName));

			Query<Long> query = s.createQuery(criteriaQuery);
			amount = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Existance - TECurrency").replace("{criteria}", currencyName));
			throw e;
		} finally {
			s.close();
		}
		return amount > 0;
	}

	public static boolean checkExistanceTECurrency(String currencyName, String worldName) {
		logger.log(Level.INFO, "Checking existance of currency by name: " + currencyName + "and world: " + worldName);
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		Long amount;
		TEWorld teWorld = getTEWorld(worldName);
		if (teWorld == null) {
			return false;
		}
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<TECurrency> root = criteriaQuery.from(TECurrency.class);

			criteriaQuery.select(criteriaBuilder.count(root))
					.where(
							criteriaBuilder.equal(root.get("name"), currencyName),
							criteriaBuilder.equal(root.get("world_identifier"), teWorld.getIdentifier())
					);

			Query<Long> query = s.createQuery(criteriaQuery);
			amount = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "Existance - TECurrency").replace("{criteria}", currencyName + "," + worldName));
			throw e;
		} finally {
			s.close();
		}
		return amount > 0;
	}
}
