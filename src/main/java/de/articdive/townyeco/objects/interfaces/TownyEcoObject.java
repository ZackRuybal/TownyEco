package de.articdive.townyeco.objects.interfaces;

import de.articdive.townyeco.database.HibernateDatabase;

public interface TownyEcoObject {
	default void save() {
		HibernateDatabase.saveObject(this);
	}

	default String getType() {return this.getClass().getSimpleName();}
}
