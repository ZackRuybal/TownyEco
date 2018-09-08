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

/*
 * Created by Lukas Mansour on the 7/22/18 11:08 AM.
 * This work is licensed under the "Creative Commons Attribution-NonCommercial-NoDerivative International License". (Short Code: CC BY-NC-ND 4.0 )
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

public class LiquibaseSchemaGenerator {
	private static TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static boolean prefixed = false;

	public static void liquify(Connection connection) throws LiquibaseException {
		copyfromJAR();
		expandEntity();
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		Liquibase liquibase = new Liquibase(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml", new FileSystemResourceAccessor(), database);
		liquibase.update(new Contexts(), new LabelExpression());
	}

	private static void copyfromJAR() {
		if (!prefixed) {
			try {
				(new File(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml")).getParentFile().mkdirs();
				BufferedReader reader = new BufferedReader(new InputStreamReader(main.getClass().getClassLoader().getResourceAsStream("db/db.changelog-master.xml")));
				PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(main.getDatabaseChangelogFolder() + "/db.changelog-master.xml"))));
				String str;
				while ((str = reader.readLine()) != null) {
					str = str.replace("/PLAYERS/", main.getMainConfig().getString(ConfigYMLNodes.DATABASE_TABLE_PREFIX) + "PLAYERS");
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