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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("stellar.admin")) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
            return true;
        }
        
        if (args.length < 2) {
            sendHelp(sender);
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ɢɪᴠᴇ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                double amount;
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().addBalance(target, amount);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀᴀᴅᴅᴇᴅ " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(amount) + " &ᴀᴛᴏ &ʙ" + target.getName()));
                break;
                
            case "set":
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ꜱᴇᴛ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                double setAmount;
                try {
                    setAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().setBalance(target, setAmount);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱᴇᴛ &ʙ" + target.getName() + " &ᴀʙᴀʟᴀɴᴄᴇ ᴛᴏ " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(setAmount)));
                break;
                
            case "take":
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ᴛᴀᴋᴇ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                double takeAmount;
                try {
                    takeAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                if (StellarCore.getInstance().getEconomyManager().withdraw(target, takeAmount)) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄʀᴇᴍᴏᴠᴇᴅ " + 
                        StellarCore.getInstance().getEconomyManager().formatCurrency(takeAmount) + " &ᴄꜰʀᴏᴍ &ʙ" + target.getName()));
                } else {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴅᴏᴇꜱɴ'ᴛ ʜᴀᴠᴇ ᴇɴᴏᴜɢʜ!"));
                }
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀᴄᴏʀᴇ ᴀᴅᴍɪɴ ᴄᴏᴍᴍᴀɴᴅꜱ:"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ɢɪᴠᴇ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ꜱᴇᴛ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴛᴇʟʟᴀʀᴀᴅᴍɪɴ ᴛᴀᴋᴇ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
    }
}
