// Created by Lukas Mansour on the 2018-09-11 at 18:45:49
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
@DiscriminatorValue("ServerShop")
public class TEServerShop extends TEShop {
	@Column(name = "minX", columnDefinition = "INTEGER")
	private int minX;

	@Column(name = "minY", columnDefinition = "INTEGER")
	private int minY;

	@Column(name = "minZ", columnDefinition = "INTEGER")
	private int minZ;

	@Column(name = "maxX", columnDefinition = "INTEGER")
	private int maxX;

	@Column(name = "maxY", columnDefinition = "INTEGER")
	private int maxY;

	@Column(name = "maxZ", columnDefinition = "INTEGER")
	private int maxZ;


	public TEServerShop(UUID identifier, int x0, int y0, int z0, int x1, int y1, int z1) {
		super(identifier);
		this.minX = Math.max(x0, x1);
		this.minY = Math.min(y0, y1);
		this.minZ = Math.min(z0, z1);
		this.maxX = Math.max(x0, x1);
		this.maxY = Math.max(y0, y1);
		this.maxZ = Math.max(y0, y1);
	}

	@SuppressWarnings("unused")
	private TEServerShop() {
		super();
	}

	// Getters
	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}
	// Setters
	public void setMinX(int minX) {
		this.minX = minX;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public void setMinZ(int minZ) {
		this.minZ = minZ;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}
}
