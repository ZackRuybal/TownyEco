// Created by Lukas Mansour on the 2018-09-17 at 19:29:12
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.objects.interfaces.TownyEcoObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NPCS")
public class TENPC implements TownyEcoObject {

	@Id
	@Column(name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	@Column(name = "registered", columnDefinition = "BIGINT(20)")
	private long registered;


	@ManyToMany()
	@JoinTable(name = "NPC_NPC_ACCESSIBILITY",
			joinColumns = @JoinColumn(name = "npc_name_1", columnDefinition = "VARCHAR(255)", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "npc_name_2", columnDefinition = "VARCHAR(255)", referencedColumnName = "name"))
	private List<TENPC> accessibleNPCs = new ArrayList<>();

	@ManyToMany()
	@JoinTable(name = "NPC_PLAYER_ACCESSIBILITY",
			joinColumns = @JoinColumn(name = "npc_name", columnDefinition = "VARCHAR(255)", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "player_identifier", columnDefinition = "VARCHAR(36)", referencedColumnName = "identifier"))
	private List<TEPlayer> accessiblePlayers = new ArrayList<>();

	@ManyToMany(mappedBy = "accessibleNPCs")
	private List<TENPC> npcAccessors;

	@ManyToMany(mappedBy = "accessiblePlayers")
	private List<TEPlayer> playerAccessors;


	public TENPC(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	private TENPC() {}

	// Getters
	public String getName() {
		return name;
	}

	public long getRegistered() {
		return registered;
	}

	public List<TENPC> getAccessibleNPCs() {
		return accessibleNPCs;
	}

	public List<TEPlayer> getAccessiblePlayers() {
		return accessiblePlayers;
	}

	public List<TENPC> getNpcAccessors() {
		return npcAccessors;
	}

	public List<TEPlayer> getPlayerAccessors() {
		return playerAccessors;
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setRegistered(long registered) {
		this.registered = registered;
	}

	public void setAccessibleNPCs(List<TENPC> accessibleNPCs) {
		this.accessibleNPCs = accessibleNPCs;
	}

	public void setAccessiblePlayers(List<TEPlayer> accessiblePlayers) {
		this.accessiblePlayers = accessiblePlayers;
	}

	public void setNpcAccessors(List<TENPC> npcAccessors) {
		this.npcAccessors = npcAccessors;
	}

	public void setPlayerAccessors(List<TEPlayer> playerAccessors) {
		this.playerAccessors = playerAccessors;
	}
}
