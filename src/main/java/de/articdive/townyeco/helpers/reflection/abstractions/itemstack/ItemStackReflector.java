// Created by Lukas Mansour on the 2018-09-30 at 09:48:51
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers.reflection.abstractions.itemstack;

import de.articdive.townyeco.helpers.reflection.abstractions.Reflector;
import org.bukkit.inventory.ItemStack;

public abstract class ItemStackReflector extends Reflector {
	public abstract String serializeItemStack(ItemStack itemStack);

	public abstract ItemStack deserializeItemStack(String itemStackString);
}
