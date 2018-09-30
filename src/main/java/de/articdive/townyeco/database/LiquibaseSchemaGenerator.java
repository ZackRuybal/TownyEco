// Created by Lukas Mansour on the 2018-09-08 at 18:55:19
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.database;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;

class LiquibaseSchemaGenerator {
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final String dbTablePrefix = main.getMainConfig().getString(ConfigYMLNodes.DATABASE_TABLE_PREFIX);
	private static boolean prefixed = false;

	public static void liquify(Connection connection) throws LiquibaseException {
		copyfromJAR();
		expandEntity();
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		Liquibase liquibase = new Liquibase(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml", new FileSystemResourceAccessor(), database);
		liquibase.update(new Contexts(), new LabelExpression());
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static void copyfromJAR() {
		if (!prefixed) {
			try {
				(new File(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml")).getParentFile().mkdirs();
				BufferedReader reader = new BufferedReader(new InputStreamReader(main.getClass().getClassLoader().getResourceAsStream("db/db.changelog-master.xml")));
				PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml"))));
				String str;
				while ((str = reader.readLine()) != null) {
					str = str.replace("/WORLDS/", dbTablePrefix + "WORLDS");
					str = str.replace("/PLAYERS/", dbTablePrefix  + "PLAYERS");
					str = str.replace("/PLAYER_PLAYER_ACCESSIBILITY/", dbTablePrefix  + "PLAYER_PLAYER_ACCESSIBILITY");
					str = str.replace("/NPCS/", dbTablePrefix  + "NPCS");
					str = str.replace("/NPC_NPC_ACCESSIBILITY/", dbTablePrefix  + "NPC_NPC_ACCESSIBILITY");
					str = str.replace("/PLAYER_NPC_ACCESSIBILITY/", dbTablePrefix  + "PLAYER_NPC_ACCESSIBILITY");
					str = str.replace("/NPC_PLAYER_ACCESSIBILITY/", dbTablePrefix  + "NPC_PLAYER_ACCESSIBILITY");
					str = str.replace("/CURRENCIES/", dbTablePrefix  + "CURRENCIES");
					str = str.replace("/CURRENCIES_BALANCES_PLAYERS/", dbTablePrefix  + "CURRENCIES_BALANCES_PLAYERS");
					str = str.replace("/CURRENCIES_BALANCES_NPCS/", dbTablePrefix  + "CURRENCIES_BALANCES_NPCS");
					str = str.replace("/SHOPS/", dbTablePrefix  + "SHOPS");
					str = str.replace("/TRADE_OBJECTS/", dbTablePrefix  + "TRADE_OBJECTS");
					writer.println(str);
				}
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void expandEntity() {
		if (!prefixed) {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml");

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();

				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml"));
				transformer.transform(source, result);
			} catch (SAXException | TransformerException | ParserConfigurationException | IOException e) {
				e.printStackTrace();
			}
			prefixed = true;
		}
	}
}