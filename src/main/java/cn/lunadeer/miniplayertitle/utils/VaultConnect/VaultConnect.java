package cn.lunadeer.miniplayertitle.utils.VaultConnect;

import cn.lunadeer.miniplayertitle.utils.XLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.lunadeer.miniplayertitle.utils.Misc.formatString;


public class VaultConnect implements Listener {

    public static String vaultUnavailable = "Vault not available, please install Vault or VaultUnlock to use economy features.";
    public static String economyUnavailable = "Economy plugin not found, you need to install one Economy plugin to use economy features.";
    public static String insufficientFunds = "Insufficient money, need {0} {1}, but only have {2} {1}.";

    public static VaultConnect instance;
    private VaultInterface vaultInstance = null;
    private final JavaPlugin plugin;

    public VaultConnect(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnable(ServiceRegisterEvent event) {
    }

    private void assertEconomy() throws Exception {
        if (vaultInstance == null) {
            Plugin vaultPlugin = this.plugin.getServer().getPluginManager().getPlugin("Vault");
            if (vaultPlugin == null) {
                throw new Exception(vaultUnavailable);
            }
            if (vaultPlugin.getDescription().getAuthors().contains("creatorfromhell")) {
                vaultInstance = new Vault2();
            } else {
                vaultInstance = new Vault();
            }
            if (!vaultInstance.init(plugin)) {
                vaultInstance = null;
                throw new Exception(economyUnavailable);
            }
        }
        XLogger.debug("Vault connected.");
    }

    public String currencyNamePlural() throws Exception {
        assertEconomy();
        return vaultInstance.currencyNamePlural();
    }

    public String currencyNameSingular() throws Exception {
        assertEconomy();
        return vaultInstance.currencyNameSingular();
    }

    public void withdrawPlayer(Player player, double amount) throws Exception {
        assertEconomy();
        if (amount > getBalance(player)) {
            throw new Exception(formatString(insufficientFunds, amount, currencyNamePlural(), getBalance(player)));
        }
        vaultInstance.withdrawPlayer(player, amount);
    }

    public void depositPlayer(Player player, double amount) throws Exception {
        assertEconomy();
        vaultInstance.depositPlayer(player, amount);
    }

    public double getBalance(Player player) throws Exception {
        assertEconomy();
        return vaultInstance.getBalance(player);
    }

    private static boolean foundClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
