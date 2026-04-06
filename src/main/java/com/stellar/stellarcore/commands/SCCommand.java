package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SCCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendMenu(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "menu":
                sendMenu(sender);
                break;
            case "balance":
            case "bal":
                if (!(sender instanceof Player)) return true;
                double bal = StellarCore.getInstance().getEconomyManager().getBalance((Player) sender);
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʙᴀʟᴀɴᴄᴇ: " + 
                    StellarCore.getInstance().getEconomyManager().formatCurrency(bal)));
                break;
            case "pay":
                if (!(sender instanceof Player)) return true;
                if (args.length < 3) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴜꜱᴀɢᴇ: &7/ꜱᴄ ᴘᴀʏ <ᴘʟᴀʏᴇʀ> <ᴀᴍᴏᴜɴᴛ>"));
                    return true;
                }
                Player from = (Player) sender;
                Player to = Bukkit.getPlayer(args[1]);
                if (to == null) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴘʟᴀʏᴇʀ ɴᴏᴛ ꜰᴏᴜɴᴅ!"));
                    return true;
                }
                double amount;
                try { amount = Double.parseDouble(args[2]); } catch (NumberFormatException e) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴᴠᴀʟɪᴅ ᴀᴍᴏᴜɴᴛ!"));
                    return true;
                }
                if (!StellarCore.getInstance().getEconomyManager().hasEnough(from, amount)) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜰᴜɴᴅꜱ!"));
                    return true;
                }
                StellarCore.getInstance().getEconomyManager().withdraw(from, amount);
                StellarCore.getInstance().getEconomyManager().deposit(to, amount);
                String formatted = StellarCore.getInstance().getEconomyManager().formatCurrency(amount);
                from.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀꜱᴇɴᴛ " + formatted + " &ᴀᴛᴏ &ʙ" + to.getName()));
                to.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʀᴇᴄᴇɪᴠᴇᴅ " + formatted + " &ᴀꜰʀᴏᴍ &ʙ" + from.getName()));
                break;
            case "top":
                List<Map.Entry<UUID, Double>> top = StellarCore.getInstance().getEconomyManager().getTopBalances(10);
                sender.sendMessage(TextUtils.format("&6&ʟ=== ᴛᴏᴘ 10 ʀɪᴄʜᴇꜱᴛ ==="));
                int rank = 1;
                for (Map.Entry<UUID, Double> entry : top) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(entry.getKey());
                    sender.sendMessage(TextUtils.format("&7" + rank + ". &ғ" + (p.getName() != null ? p.getName() : "Unknown") + 
                        " &7- " + StellarCore.getInstance().getEconomyManager().formatCurrency(entry.getValue())));
                    rank++;
                }
                break;
            case "daily":
                if (!(sender instanceof Player)) return true;
                StellarCore.getInstance().getEconomyManager().claimDaily((Player) sender);
                break;
            case "shop":
                if (!(sender instanceof Player)) return true;
                new ShopCommand().onCommand(sender, cmd, label, new String[0]);
                break;
            case "reload":
                if (!sender.hasPermission("stellar.admin")) {
                    sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɴᴏ ᴘᴇʀᴍɪꜱꜱɪᴏɴ!"));
                    return true;
                }
                StellarCore.getInstance().reloadConfig();
                sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀᴄᴏɴꜰɪɢ ʀᴇʟᴏᴀᴅᴇᴅ!"));
                break;
            default:
                sendMenu(sender);
                break;
        }
        return true;
    }
    
    private void sendMenu(CommandSender sender) {
        sender.sendMessage(TextUtils.format("&6&ʟ=== ꜱᴄ ᴍᴇɴᴜ ==="));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ʙᴀʟᴀɴᴄᴇ &8- &7ᴄʜᴇᴄᴋ ʙᴀʟᴀɴᴄᴇ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴘᴀʏ &8- &7ꜱᴇɴᴅ ᴍᴏɴᴇʏ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴛᴏᴘ &8- &7ᴛᴏᴘ 10 ʀɪᴄʜᴇꜱᴛ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ᴅᴀɪʟʏ &8- &7ᴄʟᴀɪᴍ ᴅᴀɪʟʏ"));
        sender.sendMessage(TextUtils.format("&7/ꜱᴄ ꜱʜᴏᴘ &8- &7ᴏᴘᴇɴ ꜱʜᴏᴘ"));
        sender.sendMessage(TextUtils.format("&8&ᴏꜱᴛᴀʀᴛᴇᴅ ʙʏ ꜱᴛᴇʟʟᴀʀᴘʀᴏᴊᴇᴄᴛ"));
    }
}
