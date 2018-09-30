// Created by Lukas Mansour on the 2018-09-11 at 18:45:54
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.objects.abstractions.TEShop;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@DiscriminatorValue("TownyShop")
public class TETownyShop extends TEShop {

	@Column(name = "x", columnDefinition = "INTEGER")
	private int x;

	@Column(name = "z", columnDefinition = "INTEGER")
	private int z;

	public TETownyShop(UUID identifier, int x, int z) {
		super(identifier);
		this.x = x;
		this.z = z;
	}

	@SuppressWarnings("unused")
	private TETownyShop() {
		super();
	}

	// Getters
	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	// Setters
	public void setX(int x) {
		this.x = x;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
