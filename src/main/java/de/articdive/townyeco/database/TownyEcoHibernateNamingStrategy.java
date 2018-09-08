// Created by Lukas Mansour on the 2018-09-08 at 18:55:23
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.database;

import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.configuration.enums.ConfigYMLNodes;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

@SuppressWarnings("unused")
public class TownyEcoHibernateNamingStrategy implements PhysicalNamingStrategy {
	private static TownyEco main = TownyEco.getPlugin(TownyEco.class);

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return name;
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return name;
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return jdbcEnvironment.getIdentifierHelper().toIdentifier(main.getMainConfig().getString(ConfigYMLNodes.DATABASE_TABLE_PREFIX) + name.getText());
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return name;
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return name;
	}
}
