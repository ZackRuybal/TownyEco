// Created by Lukas Mansour on the 2018-09-08 at 18:55:33
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.lang;

import de.articdive.commentedconfiguration.config.file.CommentedConfig;
import de.articdive.commentedconfiguration.interfaces.ConfigurationSection;
import de.articdive.commentedconfiguration.util.FileMgmt;
import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.lang.enums.LanguageNodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageHandler {
	private static final HashMap<Language, CommentedConfig> languages = new HashMap<>();
	private static final List<Language> playerLanguages = new ArrayList<>();
	private static Language pluginLanguage;
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);


	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void initialize() {
		new File(main.getLanguageFolder()).mkdirs();
		setPluginLanguage();
		setPlayerLanguages();
		loadLanguages();
	}

	private static void loadLanguages() {
		List<Language> languages = getAllLanguages();
		if (languages != null && !languages.isEmpty()) {
			for (Language lang : languages) {
				loadLanguage(lang, main.getLanguageFolder() + File.separator + lang.getName() + ".yml");
			}
		}
		if (!main.getMainConfig().getString(ConfigYMLNodes.LAST_RUN_VERSION).equals(main.getVersion())) {
			main.getMainConfig().setNode(ConfigYMLNodes.LAST_RUN_VERSION, main.getVersion());
			deleteTempFile(new File(main.getLanguageFolder() + File.separator + "tmp"));
		}
	}


	private static void loadLanguage(Language lang, String filepath) {

		File file = unpackResourceFile(filepath, "lang/" + lang.getName() + ".yml");
		if (file != null) {
			// read the config.yml into memory
			CommentedConfig language = new CommentedConfig(file);
			if (!language.load()) {
				// TODO: Logging (failed)
				return;
			}
			if (!main.getMainConfig().getString(ConfigYMLNodes.LAST_RUN_VERSION).equals(main.getVersion()) && !main.getMainConfig().getString(ConfigYMLNodes.LAST_RUN_VERSION).isEmpty()) {
				updateLanguage(lang, language);
			}
			language.save();
			languages.put(lang, language);
		}

	}

	private static List<Language> getAllLanguages() {
		List<Language> languages = new ArrayList<>(playerLanguages);
		if (!playerLanguages.contains(pluginLanguage)) {
			languages.add(pluginLanguage);
		}
		return languages;
	}

	public static List<Language> getPlayerLanguages() {
		return playerLanguages;
	}


	public static Language getPluginLanguage() {
		return pluginLanguage;
	}

	private static void setPlayerLanguages() {
		String languages = main.getMainConfig().getString(ConfigYMLNodes.PLAYER_LANGUAGES);
		if (languages.contains("|")) {
			String[] langs = languages.split("\\|");
			for (String lang : langs) {
				if (checkValidLanguage(lang)) {
					playerLanguages.add(Language.valueOf(lang.toUpperCase()));
				}
			}
		} else {
			if (checkValidLanguage(languages)) {
				playerLanguages.add(Language.valueOf(languages.toUpperCase()));
			}
		}
	}

	private static void setPluginLanguage() {
		String language = main.getMainConfig().getString(ConfigYMLNodes.PLUGIN_LANGUAGE);
		if (checkValidLanguage(language)) {
			pluginLanguage = Language.valueOf(language.toUpperCase());
		} else {
			pluginLanguage = Language.ENGLISH;
		}
	}

	private static boolean checkValidLanguage(String e) {
		return e.toLowerCase().equals(Language.ENGLISH.getName()) || e.equals(Language.GERMAN.getName());
	}

	private static void updateLanguage(Language lang, CommentedConfig languagefile) {
		// TODO: Logging.
		// Create tmp language.
		File file = unpackResourceFile(main.getLanguageFolder() + File.separator + "tmp" + System.getProperty("file.separator") + lang.getName() + ".yml", "lang/" + lang.getName() + ".yml");
		// read the tmp language into memory
		CommentedConfig language = new CommentedConfig(file);
		language.load();
		ConfigurationSection newconfigSection = language.getConfigurationSection(lang.getName());
		ConfigurationSection oldconfigSection = languagefile.getConfigurationSection(lang.getName());
		for (String oldkey : oldconfigSection.getKeys(true)) {
			if (newconfigSection.contains(oldkey)) {
				continue;
			}
			if (!newconfigSection.contains(oldkey)) {
				oldconfigSection.set(oldkey, null);
			}
		}
		for (String newkey : newconfigSection.getKeys(true)) {
			if (oldconfigSection.contains(newkey)) {
				continue;
			}
			if (!oldconfigSection.contains(newkey)) {
				oldconfigSection.set(newkey, language.getString(lang.getName() + "." + newkey));
			}
		}
	}

	public static String getString(LanguageNodes node, Language lang) {
		return languages.get(lang).getString((lang.getName() + "." + node.getNode()).toLowerCase());
	}

	public static String[] getStringArray(LanguageNodes node, Language lang) {
		return languages.get(lang).getStringList((lang.getName() + "." + node.getNode()).toLowerCase()).toArray(new String[0]);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static File unpackResourceFile(String filePath, String resource) {

		File file = new File(filePath);

		if ((file.exists()))
			return file;

		String resString;

		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Populate a new file
		resString = convertStreamToString("/" + resource);
		FileMgmt.stringToFile(resString, new File(filePath));

		return file;
	}

	private static String convertStreamToString(String name) {

		if (name != null) {
			Writer writer = new StringWriter();

			try (InputStream is = LanguageHandler.class.getResourceAsStream(name)) {
				char[] buffer = new char[1024];
				Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: Logging
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private static void deleteTempFile(File tempFile) {
		if (tempFile.isDirectory()) {
			File[] entries = tempFile.listFiles();
			if (entries != null) {
				for (File currentFile : entries) {
					deleteTempFile(currentFile);
				}
			}
			tempFile.delete();
		} else {
			tempFile.delete();
		}
	}
}
