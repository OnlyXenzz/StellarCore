package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) return true;
        StellarCore.getInstance().getEconomyManager().claimDaily((Player) s);
        return true;
    }
}
