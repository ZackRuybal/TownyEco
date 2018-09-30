// Created by Lukas Mansour on the 2018-09-30 at 12:36:54
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public interface TownyEcoCommand extends CommandExecutor, TabCompleter {
	@Override
	boolean onCommand(CommandSender sender, Command command, String label, String[] args);

	@Override
	List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
