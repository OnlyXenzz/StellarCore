package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴇᴀꜱᴇ ꜱᴘᴇᴄɪꜰʏ ᴀ ᴘʟᴀʏᴇʀ!"));
                return true;
            }
            target = (Player) sender;
        }
        
        double balance = StellarCore.getInstance().getEconomyManager().getBalance(target);
        String formatted = StellarCore.getInstance().getEconomyManager().formatCurrency(balance);
        
        if (target == sender) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜʀ ʙᴀʟᴀɴᴄᴇ: " + formatted));
        } else {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ʙ" + target.getName() + " &ᴀʙᴀʟᴀɴᴄᴇ: " + formatted));
        }
        
        return true;
    }
}
