package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴏɴʟʏ ᴘʟᴀʏᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ!"));
            return true;
        }
        
        Player p = (Player) sender;
        
        if (!StellarCore.getInstance().getConfig().getBoolean("daily_reward.enabled", true)) {
            p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴅᴀɪʟʏ ʀᴇᴡᴀʀᴅ ɪꜱ ᴅɪꜱᴀʙʟᴇᴅ!"));
            return true;
        }
        
        StellarCore.getInstance().getEconomyManager().claimDaily(p);
        return true;
    }
}
