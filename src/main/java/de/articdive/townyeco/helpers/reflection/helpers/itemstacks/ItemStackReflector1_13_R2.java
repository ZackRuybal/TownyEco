// Created by Lukas Mansour on the 2018-09-30 at 08:32:55
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.helpers.reflection.helpers.itemstacks;

import com.google.common.io.BaseEncoding;
import de.articdive.townyeco.helpers.reflection.abstractions.itemstack.ItemStackReflector;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ItemStackReflector1_13_R2 extends ItemStackReflector {
	public String serializeItemStack(ItemStack itemStack) {
		if (itemStack == null) {
			return "null";
		}

		ByteArrayOutputStream outputStream;
		try {
			Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
			Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
			Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
			Object nmsItemStack = getCBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
			getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
			outputStream = new ByteArrayOutputStream();
			getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
		} catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		return BaseEncoding.base64().encode(outputStream.toByteArray());
	}


	public ItemStack deserializeItemStack(String itemStackString) {
		if (itemStackString.equals("null")) {
			return null;
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));

		Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
		Class<?> nmsItemStackClass = getNMSClass("ItemStack");
		Object nbtTagCompound;
		ItemStack itemStack = null;
		try {
			nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
			Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
			itemStack = (ItemStack) getCBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}

		return itemStack;
	}

}
