package com.stellar.stellarcore.economy;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    
    private final Map<UUID, Double> balances = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> dailyStreak = new HashMap<>();
    private final Map<UUID, Long> lastDaily = new HashMap<>();
    private final File dataFile;
    private FileConfiguration dataConfig;
    
    public EconomyManager() {
        dataFile = new File(StellarCore.getInstance().getDataFolder(), "balances.yml");
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (Exception e) {}
        }
        loadData();
        
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!balances.containsKey(p.getUniqueId())) {
                double startBal = StellarCore.getInstance().getConfig().getDouble("economy.starting_balance", 100);
                balances.put(p.getUniqueId(), startBal);
            }
        }
    }
    
    private void loadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double balance = dataConfig.getDouble(key + ".balance", 100);
                int streak = dataConfig.getInt(key + ".daily_streak", 0);
                long last = dataConfig.getLong(key + ".last_daily", 0);
                balances.put(uuid, balance);
                dailyStreak.put(uuid, streak);
                lastDaily.put(uuid, last);
            } catch (Exception ignored) {}
        }
    }
    
    public void saveAll() {
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            String uuid = entry.getKey().toString();
            dataConfig.set(uuid + ".balance", entry.getValue());
            dataConfig.set(uuid + ".daily_streak", dailyStreak.getOrDefault(entry.getKey(), 0));
            dataConfig.set(uuid + ".last_daily", lastDaily.getOrDefault(entry.getKey(), 0L));
        }
        try { dataConfig.save(dataFile); } catch (Exception e) { e.printStackTrace(); }
    }
    
    public double getBalance(Player p) {
        return balances.getOrDefault(p.getUniqueId(), 
            StellarCore.getInstance().getConfig().getDouble("economy.starting_balance", 100));
    }
    
    public void setBalance(Player p, double amount) {
        balances.put(p.getUniqueId(), Math.max(0, amount));
        saveAll();
    }
    
    public void addBalance(Player p, double amount) {
        setBalance(p, getBalance(p) + amount);
    }
    
    public boolean hasEnough(Player p, double amount) {
        return getBalance(p) >= amount;
    }
    
    public boolean withdraw(Player p, double amount) {
        if (!hasEnough(p, amount)) return false;
        setBalance(p, getBalance(p) - amount);
        return true;
    }
    
    public void deposit(Player p, double amount) {
        addBalance(p, amount);
    }
    
    public void claimDaily(Player p) {
        long last = lastDaily.getOrDefault(p.getUniqueId(), 0L);
        long now = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;
        
        int streak = dailyStreak.getOrDefault(p.getUniqueId(), 0);
        int resetAfter = StellarCore.getInstance().getConfig().getInt("daily_reward.reset_after_days", 2);
        
        if (now - last > dayInMillis * resetAfter) {
            streak = 0;
        }
        
        if (now - last < dayInMillis) {
            p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄʏᴏᴜ ᴀʟʀᴇᴀᴅʏ ᴄʟᴀɪᴍᴇᴅ ᴛᴏᴅᴀʏ!"));
            return;
        }
        
        int baseReward = StellarCore.getInstance().getConfig().getInt("daily_reward.base_amount", 100);
        int bonusPerDay = StellarCore.getInstance().getConfig().getInt("daily_reward.bonus_per_day", 10);
        int maxBonus = StellarCore.getInstance().getConfig().getInt("daily_reward.max_bonus_days", 30);
        
        int bonus = Math.min(streak, maxBonus) * bonusPerDay;
        int totalReward = baseReward + bonus;
        
        streak++;
        dailyStreak.put(p.getUniqueId(), streak);
        lastDaily.put(p.getUniqueId(), now);
        addBalance(p, totalReward);
        
        p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀ✨ ᴅᴀɪʟʏ ʀᴇᴡᴀʀᴅ: &6⍟" + totalReward));
        p.sendMessage(TextUtils.format("&7ꜱᴛʀᴇᴀᴋ: &" + streak + " ᴅᴀʏꜱ (+⍟" + bonus + ")"));
        
        saveAll();
    }
    
    public List<Map.Entry<UUID, Double>> getTopBalances(int limit) {
        List<Map.Entry<UUID, Double>> sorted = new ArrayList<>(balances.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return sorted.subList(0, Math.min(limit, sorted.size()));
    }
    
    public String formatCurrency(double amount) {
        String symbol = StellarCore.getInstance().getConfig().getString("economy.currency_symbol", "⍟");
        return TextUtils.format("&6" + symbol + "&ᴇ" + String.format("%.2f", amount));
    }
  }
