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
