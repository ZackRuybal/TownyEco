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
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "CURRENCIES")
public class TECurrency implements TownyEcoObject {


	@Id
	@Column(name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "world_identifier")
	@Type(type = "uuid-char")
	private TEWorld world;

	@ElementCollection
	@CollectionTable(name = "CURRENCIES_BALANCES_PLAYERS")
	@MapKeyJoinColumn(name = "player_identifier")
	@Column(name = "balance")
	private Map<TEPlayer, BigDecimal> playerBalances = new HashMap<>();

	@ElementCollection
	@CollectionTable(name = "CURRENCIES_BALANCES_NPCS")
	@MapKeyJoinColumn(name = "npc_name")
	@Column(name = "balance")
	private Map<TENPC, BigDecimal> NPCBalances = new HashMap<>();


	public TECurrency(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	private TECurrency() {}

	// Getters
	public String getName() {
		return name;
	}

	public TEWorld getWorld() {
		return world;
	}

	public Map<TEPlayer, BigDecimal> getPlayerBalances() {
		return playerBalances;
	}

	public Map<TENPC, BigDecimal> getNPCBalances() {
		return NPCBalances;
	}


	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setWorld(TEWorld world) {
		this.world = world;
	}

	public void setPlayerBalances(Map<TEPlayer, BigDecimal> playerBalances) {
		this.playerBalances = playerBalances;
	}

	public void setNPCBalances(Map<TENPC, BigDecimal> NPCBalances) {
		this.NPCBalances = NPCBalances;
	}
}
