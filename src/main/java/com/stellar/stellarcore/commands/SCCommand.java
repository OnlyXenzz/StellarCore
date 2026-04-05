package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SCCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length == 0) {
            sendMainMenu(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            
            // ========== PLAYER COMMANDS ==========
            case "menu":
            case "help":
                sendMainMenu(sender);
                break;
                
            case "balance":
            case "bal":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴏɴʟʏ!"));
                    return true;
                }
                Player p = (Player) sender;
                double bal = StellarCore.getInstance().getEconomyManager().getBalance(p);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜʀ ʙᴀʟᴀɴᴄᴇ: " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(bal)));
                break;
                
            case "pay":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴏɴʟʏ!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴄ ᴘᴀʏ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                Player from = (Player) sender;
                Player to = Bukkit.getPlayer(args[1]);
                if (to == null) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
                    return true;
                }
                double amount;
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                if (!StellarCore.getInstance().getEconomyManager().hasEnough(from, amount)) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜱᴛᴇʟʟᴀʀ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().withdraw(from, amount);
                StellarCore.getInstance().getEconomyManager().deposit(to, amount);
                String formatted = StellarCore.getInstance().getEconomyManager().formatCurrency(amount);
                from.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜ ꜱᴇɴᴛ " + formatted + " &ᴀᴛᴏ &ʙ" + to.getName()));
                to.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʏᴏᴜ ʀᴇᴄᴇɪᴠᴇᴅ " + formatted + " &ᴀꜰʀᴏᴍ &ʙ" + from.getName()));
                break;
                
            case "top":
                List<java.util.Map.Entry<UUID, Double>> top = StellarCore.getInstance().getEconomyManager().getTopBalances(10);
                sender.sendMessage(TextUtils.format("&6&ʟ=== ᴛᴏᴘ 10 ʀɪᴄʜᴇꜱᴛ ꜱᴛᴇʟʟᴀʀ ==="));
                int rank = 1;
                for (java.util.Map.Entry<UUID, Double> entry : top) {
                    org.bukkit.OfflinePlayer off = Bukkit.getOfflinePlayer(entry.getKey());
                    String name = off.getName() != null ? off.getName() : "Unknown";
                    sender.sendMessage(TextUtils.format("&7" + rank + ". &ғ" + name + " &7- " + 
                        StellarCore.getInstance().getEconomyManager().formatCurrency(entry.getValue())));
                    rank++;
                }
                break;
                
            case "daily":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴏɴʟʏ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().claimDaily((Player) sender);
                break;
                
            case "shop":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴏɴʟʏ!"));
                    return true;
                }
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱʜᴏᴘ ᴄᴏᴍɪɴɢ ꜱᴏᴏɴ!"));
                break;
                
            case "status":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ᴏɴʟʏ!"));
                    return true;
                }
                Player target = (Player) sender;
                double balance = StellarCore.getInstance().getEconomyManager().getBalance(target);
                sender.sendMessage(TextUtils.format("&6&ʟ=== ʏᴏᴜʀ ꜱᴛᴀᴛᴜꜱ ==="));
                sender.sendMessage(TextUtils.format("&7ᴘʟᴀʏᴇʀ: &ғ" + target.getName()));
                sender.sendMessage(TextUtils.format("&7ʙᴀʟᴀɴᴄᴇ: " + StellarCore.getInstance().getEconomyManager().formatCurrency(balance)));
                sender.sendMessage(TextUtils.format("&7ᴘᴀᴄᴋ ꜱᴛᴀᴛᴜꜱ: &ᴀᴀᴄᴛɪᴠᴇ"));
                break;
            
            // ========== ADMIN COMMANDS ==========
            case "reload":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                StellarCore.getInstance().reloadConfig();
                sender.sendMessage(TextUtils.format("&ᴀᴄᴏɴꜰɪɢᴜʀᴀᴛɪᴏɴ ʀᴇʟᴏᴀᴅᴇᴅ!"));
                break;
                
            case "give":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴄ ɢɪᴠᴇ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                Player giveTarget = Bukkit.getPlayer(args[1]);
                if (giveTarget == null) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
                    return true;
                }
                double giveAmount;
                try {
                    giveAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().addBalance(giveTarget, giveAmount);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀᴀᴅᴅᴇᴅ " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(giveAmount) + " &ᴀᴛᴏ &ʙ" + giveTarget.getName()));
                break;
                
            case "set":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴄ ꜱᴇᴛ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                Player setTarget = Bukkit.getPlayer(args[1]);
                if (setTarget == null) {
                    sender.sendMessage(TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
                    return true;
                }
                double setAmount;
                try {
                    setAmount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().setBalance(setTarget, setAmount);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱᴇᴛ &ʙ" + setTarget.getName() + " &ᴀʙᴀʟᴀɴᴄᴇ ᴛᴏ " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(setAmount)));
                break;
                
            case "stats":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                sender.sendMessage(TextUtils.format("&6&ʟ=== ꜱᴛᴇʟʟᴀʀᴄᴏʀᴇ ꜱᴛᴀᴛɪꜱᴛɪᴄꜱ ==="));
                sender.sendMessage(TextUtils.format("&7ᴏɴʟɪɴᴇ ᴘʟᴀʏᴇʀꜱ: &ᴀ" + Bukkit.getOnlinePlayers().size()));
                sender.sendMessage(TextUtils.format("&7ᴠᴇʀꜱɪᴏɴ: &ᴀ" + StellarCore.getInstance().getDescription().getVersion()));
                break;
                
            case "debug":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                sender.sendMessage(TextUtils.format("&ᴀᴅᴇʙᴜɢ ᴍᴏᴅᴇ ᴛᴏɢɢʟᴇᴅ"));
                break;
                
            default:
                sendMainMenu(sender);
                break;
        }
        return true;
    }
    
    private void sendMainMenu(CommandSender sender) {
        sender.sendMessage(TextUtils.format("&6&ʟ=== ꜱᴄ ᴍᴇɴᴜ ==="));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ʙᴀʟᴀɴᴄᴇ &8- &7ᴄʜᴇᴄᴋ ʏᴏᴜʀ ʙᴀʟᴀɴᴄᴇ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴘᴀʏ &8- &7ꜱᴇɴᴅ ᴍᴏɴᴇʏ ᴛᴏ ᴘʟᴀʏᴇʀ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴛᴏᴘ &8- &7ᴠɪᴇᴡ ʀɪᴄʜᴇꜱᴛ ᴘʟᴀʏᴇʀꜱ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴅᴀɪʟʏ &8- &7ᴄʟᴀɪᴍ ᴅᴀɪʟʏ ʀᴇᴡᴀʀᴅ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ꜱʜᴏᴘ &8- &7ᴏᴘᴇɴ ꜱʜᴏᴘ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ꜱᴛᴀᴛᴜꜱ &8- &7ᴠɪᴇᴡ ʏᴏᴜʀ ꜱᴛᴀᴛᴜꜱ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴍᴇɴᴜ &8- &7ꜱʜᴏᴡ ᴛʜɪꜱ ᴍᴇɴᴜ"));
        
        if (sender.hasPermission("stellar.admin")) {
            sender.sendMessage(TextUtils.format("&6&ʟᴀᴅᴍɪɴ ᴄᴏᴍᴍᴀɴᴅꜱ:"));
            sender.sendMessage(TextUtils.format("&7/ꜱᴄ ʀᴇʟᴏᴀᴅ &8- &7ʀᴇʟᴏᴀᴅ ᴄᴏɴꜰɪɢ"));
            sender.sendMessage(TextUtils.format("&7/ꜱᴄ ɢɪᴠᴇ &8- &7ɢɪᴠᴇ ᴍᴏɴᴇʏ ᴛᴏ ᴘʟᴀʏᴇʀ"));
            sender.sendMessage(TextUtils.format("&7/ꜱᴄ ꜱᴇᴛ &8- &7ꜱᴇᴛ ᴘʟᴀʏᴇʀ ʙᴀʟᴀɴᴄᴇ"));
            sender.sendMessage(TextUtils.format("&7/ꜱᴄ ꜱᴛᴀᴛꜱ &8- &7ᴠɪᴇᴡ ᴘʟᴜɢɪɴ ꜱᴛᴀᴛɪꜱᴛɪᴄꜱ"));
            sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴅᴇʙᴜɢ &8- &7ᴛᴏɢɢʟᴇ ᴅᴇʙᴜɢ ᴍᴏᴅᴇ"));
        }
        
        sender.sendMessage(TextUtils.format("&8&ᴏꜱᴛᴀʀᴛᴇᴅ ʙʏ ꜱᴛᴇʟʟᴀʀᴘʀᴏᴊᴇᴄᴛ"));
    }
}
