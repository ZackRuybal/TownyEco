// Created by Lukas Mansour on the 2018-09-09 at 10:07:12
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "WORLDS")
public class TEWorld implements TownyEcoObject {

	@Id
	@Column(name = "identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID identifier;

	@Column(name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	public TEWorld(UUID identifier) {
		this.identifier = identifier;
	}

	@SuppressWarnings("unused")
	private TEWorld() {}

	// Getters
	public UUID getIdentifier() {
		return identifier;
	}

	public String getName() { return name; }

	// Setters
	private void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setName(String name) {
		this.name = name;
	}
}
