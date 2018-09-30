// Created by Lukas Mansour on the 2018-09-26 at 17:00:08
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects.trade_objects.abstractions;

import de.articdive.townyeco.objects.abstractions.TEShop;
import de.articdive.townyeco.objects.interfaces.TownyEcoObject;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TRADE_OBJECTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "displayName", "shop_identifier", "tradeObjectType"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tradeObjectType")
public abstract class TETradeObject implements Comparable<TETradeObject>, TownyEcoObject {
	@Id
	@Column(name = "identifier", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID identifier;

	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
	private String name;

	@Column(name = "displayName", nullable = false, columnDefinition = "VARCHAR(255)")
	private String displayName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shop_identifier", nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private TEShop shop;

	@Column(name = "dynamic", columnDefinition = "BIT(1)")
	private boolean dynamic;

	@Column(name = "staticPrice", columnDefinition = "DECIMAL")
	private BigDecimal staticPrice;

	@Column(name = "price", columnDefinition = "DECIMAL")
	private BigDecimal price;

	@Column(name = "median", columnDefinition = "DECIMAL")
	private BigDecimal median;

	@Column(name = "startPrice", columnDefinition = "DECIMAL")
	private BigDecimal startPrice;

	@Column(name = "maxPrice", columnDefinition = "DECIMAL")
	private BigDecimal maxPrice;

	@Column(name = "minPrice", columnDefinition = "DECIMAL")
	private BigDecimal minPrice;

	@Column(name = "stock", columnDefinition = "INTEGER")
	private int stock;

	@Column(name = "maxStock", columnDefinition = "INTEGER")
	private int maxStock;

	protected TETradeObject(UUID identifier, String name, String displayName, TEShop shop) {
		this.identifier = identifier;
		this.name = name;
		this.displayName = displayName;
		this.shop = shop;
	}

	@SuppressWarnings("unused")
	protected TETradeObject() {}

	// TODO:
	//	protected ArrayList<String> aliases = new ArrayList<>();
	//	protected ArrayList<String> categories = new ArrayList<>();

	@Override
	public int compareTo(TETradeObject townyEcoTradeObject) {
		if (townyEcoTradeObject == null) return 1;
		String d1;
		String d2;
		if (getDisplayName() == null) {
			d1 = getName();
		} else {
			d1 = getDisplayName();
		}
		if (townyEcoTradeObject.getDisplayName() == null) {
			d2 = townyEcoTradeObject.getName();
		} else {
			d2 = townyEcoTradeObject.getDisplayName();
		}
		if (d1 == null) {
			return -1;
		}
		if (d2 == null) {
			return 1;
		}
		return d1.compareTo(d2);
	}

	// GETTERS
	public UUID getIdentifier() {
		return identifier;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public TEShop getShop() {
		return shop;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public BigDecimal getStaticPrice() {
		return staticPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getMedian() {
		return median;
	}

	public BigDecimal getStartPrice() {
		return startPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public int getStock() {
		return stock;
	}

	public int getMaxStock() {
		return maxStock;
	}

	// SETTERS
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setShop(TEShop shop) {
		this.shop = shop;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public void setStaticPrice(BigDecimal staticPrice) {
		this.staticPrice = staticPrice;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setMedian(BigDecimal median) {
		this.median = median;
	}

	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
	}
}
