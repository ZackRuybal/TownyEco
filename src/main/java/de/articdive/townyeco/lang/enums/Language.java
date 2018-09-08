// Created by Lukas Mansour on the 2018-09-08 at 18:55:26
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.lang.enums;

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
