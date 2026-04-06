package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) return true;
        double bal = StellarCore.getInstance().getEconomyManager().getBalance((Player) s);
        s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʙᴀʟᴀɴᴄᴇ: " + 
            StellarCore.getInstance().getEconomyManager().formatCurrency(bal)));
        return true;
    }
}
