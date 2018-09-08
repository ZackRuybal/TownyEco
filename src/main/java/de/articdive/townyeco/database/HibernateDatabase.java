package de.articdive.townyeco.database;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.LanguageNodes;
import de.articdive.townyeco.objects.TEPlayer;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
 * Created by Lukas Mansour on the 7/22/18 8:47 AM.
 * This work is licensed under the "Creative Commons Attribution-NonCommercial-NoDerivative International License". (Short Code: CC BY-NC-ND 4.0 )
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

public class HibernateDatabase {
	private static SessionFactory sessionFactory;
	private static final Logger logger = LogManager.getLogger("de.articdive.metropoles.database");
	private static TownyEco main = TownyEco.getPlugin(TownyEco.class);
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
		appenders.add(FileAppender.createAppender(main.getRootFolder() + "/logs/Metropoles.log", "false", "false", "FileMetropoles", "true", "false", "false", "0", layout, null, "false", null, config));

		for (Appender appender : appenders) {
			appender.start();
			config.addAppender(appender);
			appendersreferences.add(new AppenderRef[]{AppenderRef.createAppenderRef(appender.getName(), null, null)});
		}

		LoggerConfig hibernateLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "org.hibernate", "false", appendersreferences.get(0), null, config, null);
		hibernateLoggerConfig.addAppender(appenders.get(0), null, null);
		LoggerConfig liquibaseLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "liquibase", "false", appendersreferences.get(1), null, config, null);
		liquibaseLoggerConfig.addAppender(appenders.get(1), null, null);
		LoggerConfig hikariCPLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "com.zaxxer.hikari", "false", appendersreferences.get(2), null, config, null);
		hikariCPLoggerConfig.addAppender(appenders.get(2), null, null);
		LoggerConfig metropolesLoggerConfig = LoggerConfig.createLogger(false, Level.ALL, "de.articdive.metropoles.database", "false", appendersreferences.get(3), null, config, null);
		metropolesLoggerConfig.addAppender(appenders.get(3), null, null);
		if (!main.getMainConfig().getBoolean(ConfigYMLNodes.LOGGING_FILE_ENABLED)) {
			hibernateLoggerConfig.setLevel(Level.OFF);
			liquibaseLoggerConfig.setLevel(Level.OFF);
			hikariCPLoggerConfig.setLevel(Level.OFF);
			metropolesLoggerConfig.setLevel(Level.OFF);
		}
		config.addLogger("org.hibernate", hibernateLoggerConfig);
		config.addLogger("liquibase", liquibaseLoggerConfig);
		config.addLogger("com.zaxxer.hikari", hikariCPLoggerConfig);
		config.addLogger("de.articdive.metropoles.database", metropolesLoggerConfig);
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
		logger.log(Level.INFO, "Closing SessionFactory");
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
	public static List<TEPlayer> getAllMetropolitains() {
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


	// GET METHODS
	public static TEPlayer getTEPlayer(UUID identifier) {
		logger.log(Level.INFO, "Getting TEPlayer by identifier: " + identifier.toString());
		TEPlayer metropolitain = null;
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			metropolitain = s.get(TEPlayer.class, identifier);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "metropolitain").replace("{criteria}", identifier.toString()));
		} finally {
			s.close();
		}
		return metropolitain;
	}

	public static List<TEPlayer> getTEPlayer(String lastKnownName) {
		logger.log(Level.INFO, "Getting TEPlayer(s) by lastKnownName: " + lastKnownName);
		List<TEPlayer> metropolitains = new ArrayList<>();
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			CriteriaBuilder criteriaBuilder = s.getCriteriaBuilder();
			CriteriaQuery<TEPlayer> criteriaQuery = criteriaBuilder.createQuery(TEPlayer.class);
			Root<TEPlayer> root = criteriaQuery.from(TEPlayer.class);
			criteriaQuery.select(root)
					.where(criteriaBuilder.equal(root.get("lastKnownName"), lastKnownName));
			TypedQuery<TEPlayer> query = s.createQuery(criteriaQuery);
			metropolitains = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_LOAD_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "metropolitain").replace("{criteria}", lastKnownName));
		} finally {
			s.close();
		}
		return metropolitains;
	}

	// DELETE METHODS
	public static void deleteTEPlayer(TEPlayer player) {
		logger.log(Level.INFO, "Deleting TEPlayer by identifier: " + player.getIdentifier().toString());
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		try {
			Object o = s.get(TEPlayer.class, player.getIdentifier());
			s.delete(o);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			main.getLogger().severe(LanguageHandler.getString(LanguageNodes.LOGGING_DATABASE_FAILED_TO_DELETE_OBJECT, LanguageHandler.getPluginLanguage()).replace("{object}", "all properties"));
			;
			throw e;
		} finally {
			s.close();
		}
	}
}
