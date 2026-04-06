package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("stellar.admin")) {
            s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
            return true;
        }
        if (a.length < 2) return true;
        Player t = Bukkit.getPlayer(a[1]);
        if (t == null) { s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!")); return true; }
        double amt;
        try { amt = Double.parseDouble(a[2]); } catch (Exception e) { return true; }
        switch (a[0].toLowerCase()) {
            case "give":
                StellarCore.getInstance().getEconomyManager().addBalance(t, amt);
                s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀɢᴀᴠᴇ " + StellarCore.getInstance().getEconomyManager().formatCurrency(amt) + " &ᴀᴛᴏ &ʙ" + t.getName()));
                break;
            case "set":
                StellarCore.getInstance().getEconomyManager().setBalance(t, amt);
                s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱᴇᴛ &ʙ" + t.getName() + " &ᴀʙᴀʟᴀɴᴄᴇ ᴛᴏ " + StellarCore.getInstance().getEconomyManager().formatCurrency(amt)));
                break;
            case "take":
                if (StellarCore.getInstance().getEconomyManager().withdraw(t, amt)) {
                    s.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄʀᴇᴍᴏᴠᴇᴅ " + StellarCore.getInstance().getEconomyManager().formatCurrency(amt) + " &ᴄꜰʀᴏᴍ &ʙ" + t.getName()));
                }
                break;
        }
        return true;
    }
}
