// Created by Lukas Mansour on the 2018-09-30 at 08:37:41
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers.reflection.abstractions;

import de.articdive.townyeco.TownyEco;

public abstract class Reflector {
	protected static final TownyEco main = TownyEco.getPlugin(TownyEco.class);
	private static final String minecraftVersion = main.getMinecraftVersion();

	protected Class<?> getNMSClass(String className) {
		String classLocation = "net.minecraft.server." + minecraftVersion + "." + className;
		Class<?> nmsClass = null;
		try {
			nmsClass = Class.forName(classLocation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			// TODO: Error Handling
			System.err.println("Unable to find reflection class " + classLocation + "!");
		}
		return nmsClass;
	}

	protected Class<?> getCBClass(String className) {
		String classLocation = "org.bukkit.craftbukkit." + minecraftVersion + "." + className;
		Class<?> nmsClass = null;
		try {
			nmsClass = Class.forName(classLocation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Unable to find reflection class " + classLocation + "!");
		}
		return nmsClass;
	}
}
