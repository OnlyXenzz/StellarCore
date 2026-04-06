package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player) || a.length < 2) return true;
        Player from = (Player) s;
        Player to = Bukkit.getPlayer(a[0]);
        if (to == null) { s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!")); return true; }
        double amount;
        try { amount = Double.parseDouble(a[1]); } catch (NumberFormatException e) { return true; }
        if (!StellarCore.getInstance().getEconomyManager().hasEnough(from, amount)) {
            s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜰᴜɴᴅꜱ!"));
            return true;
        }
        StellarCore.getInstance().getEconomyManager().withdraw(from, amount);
        StellarCore.getInstance().getEconomyManager().deposit(to, amount);
        String fmt = StellarCore.getInstance().getEconomyManager().formatCurrency(amount);
        from.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱᴇɴᴛ " + fmt + " &ᴀᴛᴏ &ʙ" + to.getName()));
        to.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʀᴇᴄᴇɪᴠᴇᴅ " + fmt + " &ᴀꜰʀᴏᴍ &ʙ" + from.getName()));
        return true;
    }
}
