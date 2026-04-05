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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴏɴʟʏ ᴘʟᴀʏᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ!"));
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ᴘᴀʏ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
            return true;
        }
        
        Player from = (Player) sender;
        Player to = Bukkit.getPlayer(args[0]);
        
        if (to == null) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
            return true;
        }
        
        if (from.getUniqueId().equals(to.getUniqueId())) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄʏᴏᴜ ᴄᴀɴɴᴏᴛ ᴘᴀʏ ʏᴏᴜʀꜱᴇʟꜰ!"));
            return true;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
            return true;
        }
        
        double minPay = StellarCore.getInstance().getConfig().getDouble("economy.min_pay", 1);
        
        if (amount < minPay) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴍɪɴɪᴍᴜᴍ ᴀᴍᴏᴜɴᴛ ɪꜱ " + 
                StellarCore.getInstance().getEconomyManager().formatCurrency(minPay)));
            return true;
        }
        
        if (!StellarCore.getInstance().getEconomyManager().hasEnough(from, amount)) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜰᴜɴᴅꜱ!"));
            return true;
        }
        
        StellarCore.getInstance().getEconomyManager().withdraw(from, amount);
        StellarCore.getInstance().getEconomyManager().deposit(to, amount);
        
        String formattedAmount = StellarCore.getInstance().getEconomyManager().formatCurrency(amount);
        
        from.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜ ꜱᴇɴᴛ " + formattedAmount + " &ᴀᴛᴏ &ʙ" + to.getName()));
        to.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜ ʀᴇᴄᴇɪᴠᴇᴅ " + formattedAmount + " &ᴀꜰʀᴏᴍ &ʙ" + from.getName()));
        
        return true;
    }
}
