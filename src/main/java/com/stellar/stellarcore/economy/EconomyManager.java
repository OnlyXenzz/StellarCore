package com.stellar.stellarcore.economy;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {
    
    private final Map<UUID, Double> balances = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastDaily = new HashMap<>();
    private final Map<UUID, Integer> dailyStreak = new HashMap<>();
    private final File dataFile;
    
    public EconomyManager() {
        dataFile = new File(StellarCore.getInstance().getDataFolder(), "balances.yml");
        loadData();
    }
    
    private void loadData() {
        if (!dataFile.exists()) return;
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                balances.put(uuid, config.getDouble(key + ".balance", 100.0));
                dailyStreak.put(uuid, config.getInt(key + ".streak", 0));
                lastDaily.put(uuid, config.getLong(key + ".last_daily", 0));
            } catch (Exception ignored) {}
        }
    }
    
    public void saveData() {
        YamlConfiguration config = new YamlConfiguration();
        for (UUID uuid : balances.keySet()) {
            config.set(uuid.toString() + ".balance", balances.get(uuid));
            config.set(uuid.toString() + ".streak", dailyStreak.getOrDefault(uuid, 0));
            config.set(uuid.toString() + ".last_daily", lastDaily.getOrDefault(uuid, 0L));
        }
        try { config.save(dataFile); } catch (Exception e) { e.printStackTrace(); }
    }
    
    public double getBalance(Player p) {
        return balances.getOrDefault(p.getUniqueId(), 
            StellarCore.getInstance().getConfig().getDouble("economy.starting_balance", 100.0));
    }
    
    public void setBalance(Player p, double amount) {
        balances.put(p.getUniqueId(), Math.max(0, amount));
        saveData();
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
        long now = System.currentTimeMillis();
        long last = lastDaily.getOrDefault(p.getUniqueId(), 0L);
        long dayMillis = 24 * 60 * 60 * 1000;
        
        if (now - last < dayMillis) {
            long remaining = dayMillis - (now - last);
            long hours = remaining / (60 * 60 * 1000);
            p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄʏᴏᴜ ᴄᴀɴ ᴄʟᴀɪᴍ ᴀɢᴀɪɴ ɪɴ &ᴇ" + hours + " &ᴄʜᴏᴜʀꜱ"));
            return;
        }
        
        int streak = dailyStreak.getOrDefault(p.getUniqueId(), 0);
        if (now - last > dayMillis * 2) streak = 0;
        
        double baseReward = StellarCore.getInstance().getConfig().getDouble("daily_reward.base_amount", 100.0);
        double bonusPerDay = StellarCore.getInstance().getConfig().getDouble("daily_reward.bonus_per_day", 10.0);
        int maxBonus = StellarCore.getInstance().getConfig().getInt("daily_reward.max_bonus_days", 30);
        
        int bonusDays = Math.min(streak, maxBonus);
        double totalReward = baseReward + (bonusDays * bonusPerDay);
        
        streak++;
        dailyStreak.put(p.getUniqueId(), streak);
        lastDaily.put(p.getUniqueId(), now);
        addBalance(p, totalReward);
        
        p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀ✨ ᴅᴀɪʟʏ ʀᴇᴡᴀʀᴅ: " + formatCurrency(totalReward)));
        p.sendMessage(TextUtils.format("&7ꜱᴛʀᴇᴀᴋ: &ᴇ" + streak + " &7ᴅᴀʏꜱ (+" + formatCurrency(bonusDays * bonusPerDay) + ")"));
        
        saveData();
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
    
    public void applyInterestToAll() {
        if (!StellarCore.getInstance().getConfig().getBoolean("interest.enabled", true)) return;
        
        double rate = StellarCore.getInstance().getConfig().getDouble("interest.rate", 1.5);
        double maxInterest = StellarCore.getInstance().getConfig().getDouble("interest.max_interest", 1000.0);
        double minBalance = StellarCore.getInstance().getConfig().getDouble("interest.min_balance", 100.0);
        
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            if (entry.getValue() >= minBalance) {
                double interest = entry.getValue() * (rate / 100);
                if (interest > maxInterest) interest = maxInterest;
                balances.put(entry.getKey(), entry.getValue() + interest);
                
                Player p = Bukkit.getPlayer(entry.getKey());
                if (p != null && p.isOnline()) {
                    p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀ✨ ɪɴᴛᴇʀᴇꜱᴛ: " + formatCurrency(interest)));
                }
            }
        }
        saveData();
    }
    
    public int getTotalPlayers() {
        return balances.size();
    }
}
