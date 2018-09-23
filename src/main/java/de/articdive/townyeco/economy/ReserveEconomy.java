// Created by Lukas Mansour on the 2018-09-16 at 16:45:43
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.economy;


import de.articdive.townyeco.TownyEco;
import de.articdive.townyeco.database.HibernateDatabase;
import de.articdive.townyeco.objects.TENPC;
import de.articdive.townyeco.objects.TEPlayer;
import net.tnemc.core.economy.EconomyAPI;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;

public class ReserveEconomy implements EconomyAPI {

	@Override
	public String name() {
		return "TownyEco";
	}

	@Override
	public String version() {
		return "0.1.0.10";
	}

	@Override
	public boolean enabled() {
		return TownyEco.reserveEnabled;
	}

	@Override
	public String currencyDefaultPlural() {
		return HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getName();
	}

	@Override
	public String currencyDefaultSingular() {
		return HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getName();
	}

	@Override
	public String currencyDefaultPlural(String worldName) {
		return HibernateDatabase.getTEWorld(worldName).getMainCurrency().getName();
	}

	@Override
	public String currencyDefaultSingular(String worldName) {
		return HibernateDatabase.getTEWorld(worldName).getMainCurrency().getName();
	}

	@Override
	public boolean hasCurrency(String currencyName) {
		return HibernateDatabase.checkExistanceTECurrency(currencyName);
	}

	@Override
	public boolean hasCurrency(String currencyName, String worldName) {
		return HibernateDatabase.checkExistanceTECurrency(currencyName, worldName);
	}

	@Override
	public boolean hasAccount(String accountName) {
		return HibernateDatabase.checkExistanceTENPC(accountName);
	}

	@Override
	public boolean hasAccount(UUID accountIdentifier) {
		return HibernateDatabase.checkExistanceTEPlayer(accountIdentifier);
	}

	@Override
	public boolean createAccount(String accountName) {
		if (!(HibernateDatabase.checkExistanceTENPC(accountName))) {
			TENPC teNPC = new TENPC(accountName);
			teNPC.setRegistered(System.currentTimeMillis());
			HibernateDatabase.saveObject(teNPC);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean createAccount(UUID accountIdentifier) {
		return false;
	}

	@Override
	public boolean deleteAccount(String accountName) {
		if (HibernateDatabase.checkExistanceTENPC(accountName)) {
			TENPC teNPC = HibernateDatabase.getTENPC(accountName);
			HibernateDatabase.deleteTENPC(teNPC);
			return true;
		}
		return false;
	}


	@Override
	public boolean deleteAccount(UUID accountIdentifier) {
		if (HibernateDatabase.checkExistanceTEPlayer(accountIdentifier)) {
			TEPlayer tePlayer = HibernateDatabase.getTEPlayer(accountIdentifier);
			HibernateDatabase.deleteTEPlayer(tePlayer);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isAccessor(String associatedAccountName, String accessorAccountName) {
		if (!(hasAccount(associatedAccountName) && hasAccount(accessorAccountName))) {
			return false;
		} else {
			TENPC associatedNPC = HibernateDatabase.getTENPC(associatedAccountName);
			TENPC accessorNPC = HibernateDatabase.getTENPC(accessorAccountName);
			return associatedNPC.getNpcAccessors().contains(accessorNPC) && accessorNPC.getAccessibleNPCs().contains(associatedNPC);
		}
	}

	@Override
	public boolean isAccessor(String associatedAccountName, UUID accessorIdentifier) {
		if (!(hasAccount(associatedAccountName) && hasAccount(accessorIdentifier))) {
			return false;
		} else {
			TENPC associatedNPC = HibernateDatabase.getTENPC(associatedAccountName);
			TEPlayer accessorPlayer = HibernateDatabase.getTEPlayer(accessorIdentifier);
			return associatedNPC.getPlayerAccessors().contains(accessorPlayer) && accessorPlayer.getAccessibleNPCs().contains(associatedNPC);
		}
	}

	@Override
	public boolean isAccessor(UUID associatedIdentifier, String accessorAccountName) {
		if (!(hasAccount(associatedIdentifier) && hasAccount(accessorAccountName))) {
			return false;
		} else {
			TEPlayer associatedPlayer = HibernateDatabase.getTEPlayer(associatedIdentifier);
			TENPC accessorNPC = HibernateDatabase.getTENPC(accessorAccountName);
			return associatedPlayer.getNpcAccessors().contains(accessorNPC) && accessorNPC.getAccessiblePlayers().contains(associatedPlayer);
		}
	}

	@Override
	public boolean isAccessor(UUID associatedIdentifier, UUID accessorIdentifier) {
		if (!(hasAccount(associatedIdentifier) && hasAccount(associatedIdentifier))) {
			return false;
		} else {
			TEPlayer associatedPlayer = HibernateDatabase.getTEPlayer(associatedIdentifier);
			TEPlayer accessorPlayer = HibernateDatabase.getTEPlayer(accessorIdentifier);
			return associatedPlayer.getPlayerAccessors().contains(accessorPlayer) && accessorPlayer.getAccessiblePlayers().contains(associatedPlayer);
		}
	}

	@Override
	public boolean canWithdraw(String associatedAccountName, String accessorAccountName) {
		return isAccessor(associatedAccountName, accessorAccountName);
	}

	@Override
	public boolean canWithdraw(String associatedAccountName, UUID accessorIdentifier) {
		return isAccessor(associatedAccountName, accessorIdentifier);
	}

	@Override
	public boolean canWithdraw(UUID associatedIdentifier, String accessorAccountName) {
		return isAccessor(associatedIdentifier, accessorAccountName);
	}

	@Override
	public boolean canWithdraw(UUID associatedIdentifier, UUID accessorIdentifier) {
		return isAccessor(associatedIdentifier, accessorIdentifier);
	}

	@Override
	public boolean canDeposit(String associatedAccountName, String accessorAccountName) {
		return isAccessor(associatedAccountName, accessorAccountName);
	}

	@Override
	public boolean canDeposit(String associatedAccountName, UUID accessorIdentifier) {
		return isAccessor(associatedAccountName, accessorIdentifier);
	}

	@Override
	public boolean canDeposit(UUID associatedIdentifier, String accessorAccountName) {
		return isAccessor(associatedIdentifier, accessorAccountName);
	}

	@Override
	public boolean canDeposit(UUID associatedIdentifier, UUID accessorIdentifier) {
		return isAccessor(associatedIdentifier, accessorIdentifier);
	}

	@Override
	public BigDecimal getHoldings(String accountName) {
		return HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getNPCBalances().get(HibernateDatabase.getTENPC(accountName));
	}

	@Override
	public BigDecimal getHoldings(UUID accountIdentifier) {
		return HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getPlayerBalances().get(HibernateDatabase.getTEPlayer(accountIdentifier));
	}

	@Override
	public BigDecimal getHoldings(String accountName, String worldName) {
		return HibernateDatabase.getTEWorld(worldName).getMainCurrency().getNPCBalances().get(HibernateDatabase.getTENPC(accountName));
	}

	@Override
	public BigDecimal getHoldings(UUID accountIdentifier, String worldName) {
		return HibernateDatabase.getTEWorld(worldName).getMainCurrency().getPlayerBalances().get(HibernateDatabase.getTEPlayer(accountIdentifier));
	}

	@Override
	public BigDecimal getHoldings(String accountName, String worldName, String currencyName) {
		return HibernateDatabase.getTECurrency(currencyName, worldName).get(0).getNPCBalances().get(HibernateDatabase.getTENPC(accountName));
	}

	@Override
	public BigDecimal getHoldings(UUID accountIdentifier, String worldName, String currencyName) {
		return HibernateDatabase.getTECurrency(currencyName, worldName).get(0).getPlayerBalances().get(HibernateDatabase.getTEPlayer(accountIdentifier));
	}

	@Override
	public boolean hasHoldings(String accountName, BigDecimal amount) {
		return getHoldings(accountName).compareTo(amount) >= 0;
	}

	@Override
	public boolean hasHoldings(UUID accountIdentifier, BigDecimal amount) {
		return getHoldings(accountIdentifier).compareTo(amount) >= 0;
	}

	@Override
	public boolean hasHoldings(String accountName, BigDecimal amount, String worldName) {
		return getHoldings(accountName, worldName).compareTo(amount) >= 0;
	}

	@Override
	public boolean hasHoldings(UUID accountIdentifier, BigDecimal amount, String worldName) {
		return getHoldings(accountIdentifier, worldName).compareTo(amount) >= 0;
	}

	@Override
	public boolean hasHoldings(String accountName, BigDecimal amount, String worldName, String currencyName) {
		return getHoldings(accountName, worldName, currencyName).compareTo(amount) >= 0;
	}

	@Override
	public boolean hasHoldings(UUID accountIdentifier, BigDecimal amount, String worldName, String currencyName) {
		return getHoldings(accountIdentifier, worldName, currencyName).compareTo(amount) >= 0;
	}

	@Override
	public boolean setHoldings(String accountName, BigDecimal amount) {
		HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getNPCBalances().put(HibernateDatabase.getTENPC(accountName), amount);
		return true;
	}

	@Override
	public boolean setHoldings(UUID accountIdentifier, BigDecimal amount) {
		HibernateDatabase.getTEWorld(Bukkit.getWorlds().get(0).getUID()).getMainCurrency().getPlayerBalances().put(HibernateDatabase.getTEPlayer(accountIdentifier), amount);
		return true;
	}

	@Override
	public boolean setHoldings(String accountName, BigDecimal amount, String worldName) {
		HibernateDatabase.getTEWorld(worldName).getMainCurrency().getNPCBalances().put(HibernateDatabase.getTENPC(accountName), amount);
		return true;
	}

	@Override
	public boolean setHoldings(UUID accountIdentifier, BigDecimal amount, String worldName) {
		HibernateDatabase.getTEWorld(worldName).getMainCurrency().getPlayerBalances().put(HibernateDatabase.getTEPlayer(accountIdentifier), amount);
		return false;
	}

	@Override
	public boolean setHoldings(String accountName, BigDecimal amount, String worldName, String currencyName) {
		HibernateDatabase.getTECurrency(currencyName, worldName).get(0).getNPCBalances().put(HibernateDatabase.getTENPC(accountName), amount);
		return true;
	}

	@Override
	public boolean setHoldings(UUID accountIdentifier, BigDecimal amount, String worldName, String currencyName) {
		HibernateDatabase.getTECurrency(currencyName, worldName).get(0).getPlayerBalances().put(HibernateDatabase.getTEPlayer(accountIdentifier), amount);
		return true;
	}

	@Override
	public boolean addHoldings(String accountName, BigDecimal amount) {
		getHoldings(accountName).add(amount);
		return true;
	}

	@Override
	public boolean addHoldings(UUID accountIdentifier, BigDecimal amount) {
		getHoldings(accountIdentifier).add(amount);
		return true;
	}

	@Override
	public boolean addHoldings(String accountName, BigDecimal amount, String worldName) {
		getHoldings(accountName, worldName).add(amount);
		return true;
	}

	@Override
	public boolean addHoldings(UUID accountIdentifier, BigDecimal amount, String worldName) {
		getHoldings(accountIdentifier, worldName).add(amount);
		return true;
	}

	@Override
	public boolean addHoldings(String accountName, BigDecimal amount, String worldName, String currencyName) {
		getHoldings(accountName, worldName, currencyName).add(amount);
		return true;
	}

	@Override
	public boolean addHoldings(UUID accountIdentifier, BigDecimal amount, String worldName, String currencyName) {
		getHoldings(accountIdentifier, worldName, currencyName).add(amount);
		return true;
	}

	@Override
	public boolean canAddHoldings(String accountName, BigDecimal bigDecimal) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canAddHoldings(UUID accountIdentifier, BigDecimal bigDecimal) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public boolean canAddHoldings(String accountName, BigDecimal bigDecimal, String world) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canAddHoldings(UUID accountIdentifier, BigDecimal bigDecimal, String s) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public boolean canAddHoldings(String accountName, BigDecimal bigDecimal, String s1, String s2) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canAddHoldings(UUID accountIdentifier, BigDecimal bigDecimal, String s, String s1) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public boolean removeHoldings(String accountName, BigDecimal amount) {
		getHoldings(accountName).subtract(amount);
		return true;
	}

	@Override
	public boolean removeHoldings(UUID accountIdentifier, BigDecimal amount) {
		getHoldings(accountIdentifier).subtract(amount);
		return true;
	}

	@Override
	public boolean removeHoldings(String accountName, BigDecimal amount, String worldName) {
		getHoldings(accountName, worldName).subtract(amount);
		return true;
	}

	@Override
	public boolean removeHoldings(UUID accountIdentifier, BigDecimal amount, String worldName) {
		getHoldings(accountIdentifier, worldName).subtract(amount);
		return true;
	}

	@Override
	public boolean removeHoldings(String accountName, BigDecimal amount, String worldName, String currencyName) {
		getHoldings(accountName, worldName, currencyName).subtract(amount);
		return true;
	}

	@Override
	public boolean removeHoldings(UUID accountIdentifier, BigDecimal amount, String worldName, String currencyName) {
		getHoldings(accountIdentifier, worldName, currencyName).subtract(amount);
		return true;
	}

	@Override
	public boolean canRemoveHoldings(String accountName, BigDecimal amount) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canRemoveHoldings(UUID accountIdentifier, BigDecimal amount) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public boolean canRemoveHoldings(String accountName, BigDecimal amount, String worldName) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canRemoveHoldings(UUID accountIdentifier, BigDecimal amount, String worldName) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public boolean canRemoveHoldings(String accountName, BigDecimal amount, String worldName, String currencyName) {
		return hasAccount(accountName);
	}

	@Override
	public boolean canRemoveHoldings(UUID accountIdentifier, BigDecimal bigDecimal, String worldName, String currencyName) {
		return hasAccount(accountIdentifier);
	}

	@Override
	public String format(BigDecimal bigDecimal) {
		return null;
	}

	@Override
	public String format(BigDecimal bigDecimal, String s) {
		return null;
	}

	@Override
	public String format(BigDecimal bigDecimal, String s, String s1) {
		return null;
	}

	@Override
	public boolean purgeAccounts() {
		return false;
	}

	@Override
	public boolean purgeAccountsUnder(BigDecimal bigDecimal) {
		return false;
	}
}
