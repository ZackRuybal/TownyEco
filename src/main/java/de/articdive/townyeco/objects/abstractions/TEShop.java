// Created by Lukas Mansour on the 2018-09-26 at 16:59:37
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects.abstractions;

import de.articdive.townyeco.objects.TEWorld;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import de.articdive.townyeco.objects.trade_objects.abstractions.TETradeObject;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SHOPS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "shop_type")
public abstract class TEShop implements TownyEcoObject {
	@Id
	@Column(name = "identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID identifier;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "world_identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private TEWorld world;

	protected TEShop(UUID identifier) {
		this.identifier = identifier;
	}


	@OneToMany(mappedBy = "shop")
	private List<TETradeObject> tradeObjects;

	@Column(name = "name")
	private String name;

	@Column(name = "entrance_message", columnDefinition = "TEXT")
	private String entranceMessage;

	@Column(name = "exit_message", columnDefinition = "TEXT")
	private String exitMessage;

	@SuppressWarnings("unused")
	protected TEShop() {}

	// Getters
	public UUID getIdentifier() {
		return identifier;
	}

	public TEWorld getWorld() {
		return world;
	}

	public List<TETradeObject> getTradeObjects() {
		return tradeObjects;
	}

	public String getName() {
		return name;
	}

	public String getEntranceMessage() {
		return entranceMessage;
	}

	public String getExitMessage() {
		return exitMessage;
	}

	// Setters
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setWorld(TEWorld world) {
		this.world = world;
	}

	public void setTradeObjects(List<TETradeObject> tradeObjects) {
		this.tradeObjects = tradeObjects;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEntranceMessage(String entranceMessage) {
		this.entranceMessage = entranceMessage;
	}

	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}
}
