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
        
        economyManager = new EconomyManager();
        
        // Register commands
        getCommand("sc").setExecutor(new SCCommand());
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("top").setExecutor(new TopCommand());
        getCommand("daily").setExecutor(new DailyCommand());
        getCommand("shop").setExecutor(new ShopCommand());
        
        // Register listener untuk shop
        getServer().getPluginManager().registerEvents(new ShopCommand(), this);
        
        // Start interest scheduler (every hour)
        int interval = getConfig().getInt("interest.interval_minutes", 60);
        Bukkit.getScheduler().runTaskTimer(this, () -> economyManager.applyInterestToAll(), 20L * 60 * interval, 20L * 60 * interval);
        
        // Auto-save every 5 minutes
        Bukkit.getScheduler().runTaskTimer(this, () -> economyManager.saveData(), 6000L, 6000L);
        
        getLogger().info(TextUtils.toSmallCaps("StellarCore v2.5.0 Enabled!"));
    }
    
    @Override
    public void onDisable() {
        economyManager.saveData();
        getLogger().info(TextUtils.toSmallCaps("StellarCore Disabled!"));
    }
    
    public static StellarCore getInstance() { return instance; }
    public EconomyManager getEconomyManager() { return economyManager; }
}
