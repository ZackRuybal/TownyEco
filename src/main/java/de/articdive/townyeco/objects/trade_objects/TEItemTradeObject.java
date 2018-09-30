// Created by Lukas Mansour on the 2018-09-26 at 17:01:15
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects.trade_objects;

import de.articdive.townyeco.helpers.ReflectionHelper;
import de.articdive.townyeco.objects.abstractions.TEShop;
import de.articdive.townyeco.objects.trade_objects.abstractions.TETradeObject;
import org.bukkit.inventory.ItemStack;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.UUID;

@Entity
@DiscriminatorValue("item")
public class TEItemTradeObject extends TETradeObject {

	@Column(name = "itemData", columnDefinition = "MEDIUMTEXT")
	private String itemData;

	@Transient
	private ItemStack itemStack;

	public TEItemTradeObject(UUID identifier, String name, String displayName, TEShop shop, ItemStack itemStack) {
		super(identifier, name, displayName, shop);
		this.itemData = ReflectionHelper.itemStackReflector.serializeItemStack(itemStack);
		this.itemStack = itemStack;
	}

	@SuppressWarnings("unused")
	private TEItemTradeObject() {
		super();
	}

	// Getters
	public String getItemData() {
		return itemData;
	}

	@Transient
	public ItemStack getItemStack() {
		return itemStack;
	}

	// Setters
	public void setItemData(String itemData) {
		this.itemData = itemData;
		this.itemStack = ReflectionHelper.itemStackReflector.deserializeItemStack(itemData);
	}

	@Transient
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.itemData = ReflectionHelper.itemStackReflector.serializeItemStack(itemStack);
	}
}
