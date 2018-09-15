// Created by Lukas Mansour on the 2018-09-10 at 18:30:02
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "CURRENCIES")
public class TECurrency implements TownyEcoObject {

	@Id
	@Column(name = "identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID identifier;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "world_identifier")
	@Type(type = "uuid-char")
	private TEWorld world;

	@ElementCollection
	@CollectionTable(name = "CURRENCIES_BALANCES")
	@MapKeyJoinColumn(name = "player_identifier")
	@MapKeyColumn(name = "player_identifier")
	@Column(name = "balance")
	private Map<TEPlayer, BigDecimal> balances = new HashMap<>();

	@Column(name = "name", columnDefinition = "VARCHAR(255)")
	private String name;


	public TECurrency(UUID identifier) {
		this.identifier = identifier;
	}

	@SuppressWarnings("unused")
	private TECurrency() {}

	// Getters
	public UUID getIdentifier() {
		return identifier;
	}

	public TEWorld getWorld() {
		return world;
	}

	public Map<TEPlayer, BigDecimal> getBalances() {
		return balances;
	}

	public String getName() {
		return name;
	}

	// Setters
	private void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setWorld(TEWorld world) {
		this.world = world;
	}

	public void setBalances(Map<TEPlayer, BigDecimal> balances) {
		this.balances = balances;
	}

	public void setName(String name) {
		this.name = name;
	}
}
