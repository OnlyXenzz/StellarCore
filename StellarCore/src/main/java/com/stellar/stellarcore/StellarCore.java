package com.stellar.stellarcore;

import com.stellar.stellarcore.commands.*;
import com.stellar.stellarcore.economy.EconomyManager;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class StellarCore extends JavaPlugin {
    
    private static StellarCore instance;
    private EconomyManager economyManager;
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        economyManager = new EconomyManager();
        
        // Register commands
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("top").setExecutor(new TopCommand());
        getCommand("daily").setExecutor(new DailyCommand());
        getCommand("stellaradmin").setExecutor(new AdminCommand());
        getCommand("shop").setExecutor(new ShopCommand());
        
        // Interest scheduler every 1 hour
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (getConfig().getBoolean("interest.enabled", true)) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    double balance = economyManager.getBalance(p);
                    if (balance >= getConfig().getDouble("interest.min_balance", 100)) {
                        double interest = balance * (getConfig().getDouble("interest.rate", 1.5) / 100);
                        double maxInterest = getConfig().getDouble("interest.max_interest", 1000);
                        if (interest > maxInterest) interest = maxInterest;
                        economyManager.addBalance(p, interest);
                        p.sendMessage(TextUtils.format("&a✨ You received &6⍟&e" + String.format("%.2f", interest) + " &ainterest!"));
                    }
                }
            }
        }, 72000L, 72000L); // every 1 hour
        
        getLogger().info(TextUtils.toSmallCaps("StellarCore v1.0.0 Enabled!"));
    }
    
    @Override
    public void onDisable() {
        if (economyManager != null) economyManager.saveAll();
        getLogger().info(TextUtils.toSmallCaps("StellarCore Disabled!"));
    }
    
    public static StellarCore getInstance() { return instance; }
    public EconomyManager getEconomyManager() { return economyManager; }
}
