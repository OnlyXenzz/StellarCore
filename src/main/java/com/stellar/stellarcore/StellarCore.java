package com.stellar.stellarcore;

import com.stellar.stellarcore.commands.*;
import com.stellar.stellarcore.economy.EconomyManager;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StellarCore extends JavaPlugin {
    
    private static StellarCore instance;
    private EconomyManager economyManager;
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        // Init economy manager
        economyManager = new EconomyManager();
        
        // Register commands
        registerCommands();
        
        // Register listeners (untuk shop GUI)
        registerListeners();
        
        // Start interest scheduler (every 1 hour)
        startInterestScheduler();
        
        getLogger().info(TextUtils.toSmallCaps("StellarCore v1.0.0 Enabled!"));
    }
    
    @Override
    public void onDisable() {
        if (economyManager != null) {
            economyManager.saveAll();
        }
        getLogger().info(TextUtils.toSmallCaps("StellarCore Disabled!"));
    }
    
    private void registerCommands() {
        getCommand("sc").setExecutor(new SCCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("top").setExecutor(new TopCommand());
        getCommand("daily").setExecutor(new DailyCommand());
        getCommand("stellaradmin").setExecutor(new AdminCommand());
        getCommand("shop").setExecutor(new ShopCommand());
    }
    
    private void registerListeners() {
        // REGISTER LISTENER UNTUK SHOP (INI YANG KAMU MINTA)
        getServer().getPluginManager().registerEvents(new ShopCommand(), this);
    }
    
    private void startInterestScheduler() {
        int interval = getConfig().getInt("interest.interval_minutes", 60);
        long ticks = interval * 60 * 20L;
        
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (getConfig().getBoolean("interest.enabled", true)) {
                double rate = getConfig().getDouble("interest.rate", 1.5);
                double maxInterest = getConfig().getDouble("interest.max_interest", 1000);
                double minBalance = getConfig().getDouble("interest.min_balance_for_interest", 100);
                
                for (Player p : Bukkit.getOnlinePlayers()) {
                    double balance = economyManager.getBalance(p);
                    if (balance >= minBalance) {
                        double interest = balance * (rate / 100);
                        if (interest > maxInterest) {
                            interest = maxInterest;
                        }
                        economyManager.addBalance(p, interest);
                        p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀ✨ ʏᴏᴜ ʀᴇᴄᴇɪᴠᴇᴅ " + 
                            economyManager.formatCurrency(interest) + " &ᴀɪɴᴛᴇʀᴇꜱᴛ!"));
                    }
                }
            }
        }, ticks, ticks);
    }
    
    public static StellarCore getInstance() {
        return instance;
    }
    
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
