package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TopCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<Map.Entry<UUID, Double>> top = StellarCore.getInstance().getEconomyManager().getTopBalances(10);
        
        sender.sendMessage(TextUtils.format("&6&ʟ=== ᴛᴏᴘ 10 ʀɪᴄʜᴇꜱᴛ ꜱᴛᴇʟʟᴀʀ ==="));
        int rank = 1;
        for (Map.Entry<UUID, Double> entry : top) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(entry.getKey());
            String name = p.getName() != null ? p.getName() : "Unknown";
            sender.sendMessage(TextUtils.format("&7" + rank + ". &ғ" + name + " &7- " + 
                StellarCore.getInstance().getEconomyManager().formatCurrency(entry.getValue())));
            rank++;
        }
        return true;
    }
}
