package de.articdive.townyeco.lang.enums;

/*
 * Created by Lukas Mansour on the 8/2/18 1:11 PM.
 * This work is licensed under the "Creative Commons Attribution-NonCommercial-NoDerivative International License". (Short Code: CC BY-NC-ND 4.0 )
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

public enum Language {
	ENGLISH("english"),
	GERMAN("german");

	private final String name;

	Language(String root) {

		this.name = root;
	}

	/**
	 * Retrieves the name for a language
	 *
	 * @return The name of a language.
	 */
	public String getName() {

		return name;
	}
}
