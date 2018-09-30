// Created by Lukas Mansour on the 2018-09-30 at 08:53:20
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.helpers.reflection.abstractions.itemstack.ItemStackReflector;
import de.articdive.townyeco.helpers.reflection.helpers.itemstacks.ItemStackReflector1_13_R2;

public class ReflectionHelper {
	private static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final String minecraftVersion = main.getMinecraftVersion();
	public static ItemStackReflector itemStackReflector;

	public static void initialize() {
		switch (minecraftVersion) {
			case "v1_13_R2": {
				itemStackReflector = new ItemStackReflector1_13_R2();
			}
		}
	}

}
