// Created by Lukas Mansour on the 2018-09-08 at 18:56:20
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
@Table(name = "PLAYERS")
public class TEPlayer implements TownyEcoObject {

	@Id
	@Column(name = "identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID identifier;

	@Column(name = "lastKnownName", columnDefinition = "VARCHAR(255)")
	private String lastKnownName;

	public TEPlayer(UUID identifier) {
		this.identifier = identifier;
	}

	@SuppressWarnings("unused")
	private TEPlayer() {}

	// Getters
	public UUID getIdentifier() {
		return identifier;
	}

	public String getLastKnownName() {
		return lastKnownName;
	}

	// Setters
	public void setLastKnownName(String lastKnownName) {
		this.lastKnownName = lastKnownName;
	}
}
