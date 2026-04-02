package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Utils.BankConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;

public class VaultHook implements Economy {

    @Override
    public boolean accountSupportsCurrency(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accountSupportsCurrency'");
    }

    @Override
    public boolean accountSupportsCurrency(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accountSupportsCurrency'");
    }

    @Override
    public boolean addAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAccountMember'");
    }

    @Override
    public boolean addAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission... arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAccountMember'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createAccount(@NotNull UUID playerUUID, @NotNull String playerName, boolean arg2) {
        // TODO: Use this method to create accounts with the option for a default
        // balance
        if (hasAccount(playerUUID)) {
            return false; // Account already exists -- No need to create one.
        }
        PlayerConfigManager playerConfigManager = new PlayerConfigManager(playerUUID);
        playerConfigManager.updatePlayerDataFile();
        Bukkit.getConsoleSender()
                .sendMessage(Component.text("Account created for: " + playerName).color(NamedTextColor.GREEN));
        return true;
    }

    public boolean createBankAccount(@NotNull String accountName, @NotNull UUID accountNumber) {
        // Validate inputs
        if (accountName == null || accountName.isEmpty() || accountNumber == null) {
            return false; // Invalid input
        }
        BankConfigManager bankConfigManager = new BankConfigManager(accountNumber);
        if (bankConfigManager.exists()) {
            return false; // Account already exists
        }
        bankConfigManager.createConfig(accountNumber);
        return true;

    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createAccount(@NotNull UUID arg0, @NotNull String arg1, @NotNull String arg2, boolean arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public boolean createSharedAccount(@NotNull String pluginName, @NotNull UUID accountNumber, @NotNull String accountName,
            @NotNull UUID owner) {
            throw new UnsupportedOperationException("Unimplemented method 'createSharedAccount'");
    }

    @Override
    public @NotNull Collection<String> currencies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'currencies'");
    }

    @Override
    public @NotNull String defaultCurrencyNamePlural(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultCurrencyNamePlural'");
    }

    @Override
    public @NotNull String defaultCurrencyNameSingular(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultCurrencyNameSingular'");
    }

    @Override
    public boolean deleteAccount(@NotNull String arg0, @NotNull UUID arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        BigDecimal amount = normalizeAmount(arg2);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return failure(amount, balance(arg0, arg1), "Cannot deposit a negative amount.");
        }

        PlayerConfigManager playerConfigManager = new PlayerConfigManager(arg1);
        BigDecimal updatedBalance = balance(arg0, arg1).add(amount);
        playerConfigManager.setBalance(updatedBalance);
        return success(amount, updatedBalance);
    }

    public @NotNull EconomyResponse depositBank(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull EconomyResponse deposit(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3, @NotNull BigDecimal arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal arg0) {
        return normalizeAmount(arg0).toPlainString();
    }

    @Override
    public @NotNull String format(@NotNull String arg0, @NotNull BigDecimal arg1) {
        return format(arg1);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal arg0, @NotNull String arg1) {
        return format(arg0);
    }

    @Override
    public @NotNull String format(@NotNull String arg0, @NotNull BigDecimal arg1, @NotNull String arg2) {
        return format(arg1);
    }

    @Override
    public int fractionalDigits(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fractionalDigits'");
    }

    @Override
    public Optional<String> getAccountName(@NotNull UUID arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountName'");
    }

    @Override
    @Deprecated
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1) {
        return balance(arg0, arg1);
    }

    @Override
    @Deprecated
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        return balance(arg0, arg1);
    }

    @Override
    @Deprecated
    public @NotNull BigDecimal getBalance(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3) {
        return balance(arg0, arg1);
    }

    @Override
    public @NotNull BigDecimal balance(@NotNull String arg0, @NotNull UUID arg1) {
        PlayerConfigManager playerConfigManager = new PlayerConfigManager(arg1);
        return normalizeAmount(new BigDecimal(playerConfigManager.getBalance()));
    }

    public @NotNull BigDecimal bankbBalance(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'balance'");
    }

    @Override
    public @NotNull String getDefaultCurrency(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDefaultCurrency'");
    }

    @Override
    public @NotNull String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public @NotNull Map<UUID, String> getUUIDNameMap() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUUIDNameMap'");
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        return balance(arg0, arg1).compareTo(normalizeAmount(arg2)) >= 0;
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2, @NotNull BigDecimal arg3) {
        return has(arg0, arg1, arg3);
    }

    @Override
    public boolean has(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2, @NotNull String arg3,
            @NotNull BigDecimal arg4) {
        return has(arg0, arg1, arg4);
    }

    @Override
    public boolean hasAccount(@NotNull UUID arg0) {
        PlayerConfigManager playerConfigManager = new PlayerConfigManager(arg0);
        return playerConfigManager.exists();
    }

    public boolean hasBankAccount(@NotNull UUID arg1) {
        BankConfigManager bankConfigManager = new BankConfigManager(arg1);
        return bankConfigManager.exists();
    }

    @Override
    public boolean hasAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccount'");
    }

    @Override
    public boolean hasAccountPermission(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccountPermission'");
    }

    @Override
    public boolean hasCurrency(@NotNull String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasCurrency'");
    }

    @Override
    public boolean hasMultiCurrencySupport() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasMultiCurrencySupport'");
    }

    @Override
    public boolean hasSharedAccountSupport() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasSharedAccountSupport'");
    }

    @Override
    public boolean isAccountMember(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountMember'");
    }

    @Override
    public boolean isAccountOwner(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountOwner'");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean removeAccountMember(@NotNull String arg0, @NotNull UUID accountNumber, @NotNull UUID memberUUID) {
        BankConfigManager bankConfigManager = new BankConfigManager(accountNumber);
        bankConfigManager.removeMember(memberUUID);
        return true;
    }

    @Override
    public boolean renameAccount(@NotNull UUID arg0, @NotNull String arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameAccount'");
    }

    @Override
    public boolean renameAccount(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameAccount'");
    }

    @Override
    public boolean setOwner(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOwner'");
    }

    @Override
    public boolean updateAccountPermission(@NotNull String arg0, @NotNull UUID arg1, @NotNull UUID arg2,
            @NotNull AccountPermission arg3, boolean arg4) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccountPermission'");
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull BigDecimal arg2) {
        BigDecimal amount = normalizeAmount(arg2);
        BigDecimal currentBalance = balance(arg0, arg1);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return failure(amount, currentBalance, "Cannot withdraw a negative amount.");
        }
        if (currentBalance.compareTo(amount) < 0) {
            return failure(amount, currentBalance, "Insufficient funds.");
        }

        BigDecimal updatedBalance = currentBalance.subtract(amount);
        PlayerConfigManager playerConfigManager = new PlayerConfigManager(arg1);
        playerConfigManager.setBalance(updatedBalance);
        return success(amount, updatedBalance);
    }

    public @NotNull EconomyResponse withdrawBank(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdraw'");
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull BigDecimal arg3) {
        return withdraw(arg0, arg1, arg3);
    }

    @Override
    public @NotNull EconomyResponse withdraw(@NotNull String arg0, @NotNull UUID arg1, @NotNull String arg2,
            @NotNull String arg3, @NotNull BigDecimal arg4) {
        return withdraw(arg0, arg1, arg4);
    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        }
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    private EconomyResponse success(BigDecimal amount, BigDecimal balance) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    private EconomyResponse failure(BigDecimal amount, BigDecimal balance, String message) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, message);
    }

}
