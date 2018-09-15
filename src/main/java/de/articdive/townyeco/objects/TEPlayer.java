// Created by Lukas Mansour on the 2018-09-08 at 18:56:20
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.lang.enums.Language;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	@Column(name = "registered", columnDefinition = "BIGINT(20)")
	private long registered;

	@Column(name = "lastOnline", columnDefinition = "BIGINT(20)")
	private long lastOnline;

	@Column(name = "language")
	@Enumerated(EnumType.STRING)
	private Language language;

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

	public long getRegistered() {
		return registered;
	}

	public long getLastOnline() {
		return lastOnline;
	}

	public Language getLanguage() {
		return language;
	}

	// Setters
	private void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setLastKnownName(String lastKnownName) {
		this.lastKnownName = lastKnownName;
	}

	public void setRegistered(long registered) {
		this.registered = registered;
	}

	public void setLastOnline(long lastOnline) {
		this.lastOnline = lastOnline;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
