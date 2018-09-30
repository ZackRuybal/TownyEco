// Created by Lukas Mansour on the 2018-09-30 at 12:36:29
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.commands;

import de.articdive.townyeco.commands.interfaces.TabCompletionEnum;
import de.articdive.townyeco.commands.interfaces.TownyEcoCommand;
import de.articdive.townyeco.helpers.TabValueHelper;
import de.articdive.townyeco.workers.MessageWorker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TownyEcoMainCommand implements TownyEcoCommand {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("townyeco")) {
			if (args.length == 0) {
				MessageWorker.sendMainCommandMenuMessage(sender);
				return true;
			}
			if (args.length == 1) {
				switch (args[0]) {
					case "info": {
						MessageWorker.sendMainCommandInfoMessage(sender);
						return true;
					}
					case "options": {
						MessageWorker.sendMainCommandOptionsMessage(sender);
						return true;
					}
					case "version": {
						MessageWorker.sendMainCommandVersionMessage(sender);
						return true;
					}
				}
			}
			if (args.length == 2) {
				switch (args[0] + " " + args[1]) {
					case "options language": {
						MessageWorker.sendMainCommandOptionsLanguageMessage(sender);
						return true;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("townyeco")) {
			if (args.length == 1) {
				return TabValueHelper.getValue(TabCompletion.TOWNYECO);
			}
			if (args.length == 2) {
				switch (args[0]) {
					case "info": {
						return TabValueHelper.getValue(TabCompletion.TOWNYECO_INFO);
					}
					case "version": {
						return TabValueHelper.getValue(TabCompletion.TOWNYECO_VERSION);
					}
					case "options": {
						return TabValueHelper.getValue(TabCompletion.TOWNYECO_OPTIONS);
					}
				}
			}
			if (args.length == 3) {
				switch (args[0] + " " + args[1]) {
					case "options language": {
						return TabValueHelper.getValue(TabCompletion.TOWNYECO_OPTION_LANGUAGE);
					}
				}
			}

		}
		return new ArrayList<>();
	}

	private enum TabCompletion implements TabCompletionEnum {
		TOWNYECO("info", "options", "version"),
		TOWNYECO_INFO,
		TOWNYECO_VERSION,
		TOWNYECO_OPTIONS("language"),
		TOWNYECO_OPTION_LANGUAGE("<playerlanguages>");

		String[] values;

		TabCompletion(String... values) {
			this.values = values;
		}


		@Override
		public String[] getValues() {
			return values;
		}
	}
}
