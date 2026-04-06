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
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        List<Map.Entry<UUID, Double>> top = StellarCore.getInstance().getEconomyManager().getTopBalances(10);
        s.sendMessage(TextUtils.format("&6&ʟ=== ᴛᴏᴘ 10 ʀɪᴄʜᴇꜱᴛ ==="));
        int rank = 1;
        for (Map.Entry<UUID, Double> entry : top) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(entry.getKey());
            s.sendMessage(TextUtils.format("&7" + rank + ". &ғ" + (p.getName() != null ? p.getName() : "Unknown") + 
                " &7- " + StellarCore.getInstance().getEconomyManager().formatCurrency(entry.getValue())));
            rank++;
        }
        return true;
    }
}
