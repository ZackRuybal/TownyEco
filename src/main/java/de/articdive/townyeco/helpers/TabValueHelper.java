// Created by Lukas Mansour on the 2018-09-30 at 12:52:05
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers;

import de.articdive.townyeco.commands.interfaces.TabCompletionEnum;
import de.articdive.townyeco.lang.LanguageHandler;
import de.articdive.townyeco.lang.enums.Language;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabValueHelper {
	public static List<String> getValue(TabCompletionEnum tabCompleteEnum) {
		String[] values = tabCompleteEnum.getValues();
		List<String> output = new ArrayList<>();
		for (String value : values) {
			if (value.startsWith("<") && value.endsWith(">")) {
				output.addAll(handlePlaceHolder(value));
			} else {
				output.add(value);
			}
		}
		return output;
	}

	private static List<String> handlePlaceHolder(String placeholder) {
		List<String> output = new ArrayList<>();

		// Bukkit methods:
		if (placeholder.equals("<players>")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				output.add(p.getName());
			}
		}
		if (placeholder.equals("<world>")) {
			for (World w : Bukkit.getWorlds()) {
				output.add(w.getName());
			}
		}

		// Enumerations:
		if (placeholder.equalsIgnoreCase("<playerlanguages>")) {
			return Arrays.stream(LanguageHandler.getPlayerLanguages().toArray(new Language[0])).map(language -> language.getName().substring(0, 1).toUpperCase() + language.getName().substring(1).toLowerCase()).collect(Collectors.toList());
		}

		return output;
	}
}
