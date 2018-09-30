// Created by Lukas Mansour on the 2018-09-09 at 10:07:12
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects;

import de.articdive.townyeco.objects.abstractions.TEShop;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "maincurrency_name", referencedColumnName = "name", columnDefinition = "VARCHAR(36)")
	private TECurrency mainCurrency;

	@OneToMany(mappedBy = "world")
	private List<TECurrency> currencies;

	@OneToMany(mappedBy = "world")
	private List<TEShop> shops;

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

	public TECurrency getMainCurrency() {
		return mainCurrency;
	}

	public List<TECurrency> getCurrencies() {
		return currencies;
	}

	public List<TEShop> getShops() {
		return shops;
	}

	// Setters
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMainCurrency(TECurrency mainCurrency) {
		this.mainCurrency = mainCurrency;
	}

	public void setCurrencies(List<TECurrency> currencies) {
		this.currencies = currencies;
	}

	public void setShops(List<TEShop> shops) {
		this.shops = shops;
	}
}
