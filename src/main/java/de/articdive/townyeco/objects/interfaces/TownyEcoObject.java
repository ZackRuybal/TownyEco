// Created by Lukas Mansour on the 2018-09-08 at 18:56:17
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects.interfaces;

import de.articdive.townyeco.database.HibernateDatabase;

public interface TownyEcoObject {
	default void save() {
		HibernateDatabase.saveObject(this);
	}

	default String getType() {return this.getClass().getSimpleName();}
}
