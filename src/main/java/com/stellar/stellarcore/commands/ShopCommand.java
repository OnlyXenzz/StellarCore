package com.stellar.stellarcore.commands;

import com.stellar.stellarcore.StellarCore;
import com.stellar.stellarcore.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class ShopCommand implements CommandExecutor, Listener {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        openShop((Player) sender);
        return true;
    }
    
    private void openShop(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀ ꜱʜᴏᴘ"));
        
        // 10 sample items
        addShopItem(inv, 0, Material.DIAMOND, "&ʙᴅɪᴀᴍᴏɴᴅ", 100);
        addShopItem(inv, 1, Material.EMERALD, "&ᴀᴇᴍᴇʀᴀʟᴅ", 50);
        addShopItem(inv, 2, Material.GOLD_INGOT, "&6ɢᴏʟᴅ", 20);
        addShopItem(inv, 3, Material.IRON_INGOT, "&7ɪʀᴏɴ", 10);
        addShopItem(inv, 4, Material.NETHERITE_INGOT, "&5ɴᴇᴛʜᴇʀɪᴛᴇ", 500);
        addShopItem(inv, 5, Material.ENDER_PEARL, "&5ᴇɴᴅᴇʀ ᴘᴇᴀʀʟ", 30);
        addShopItem(inv, 6, Material.TOTEM_OF_UNDYING, "&6ᴛᴏᴛᴇᴍ", 1000);
        addShopItem(inv, 7, Material.EXPERIENCE_BOTTLE, "&ᴀᴇxᴘ ʙᴏᴛᴛʟᴇ", 15);
        addShopItem(inv, 8, Material.COOKED_BEEF, "&ᴄᴄᴏᴏᴋᴇᴅ ʙᴇᴇꜰ", 8);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta meta = close.getItemMeta();
        meta.setDisplayName(TextUtils.format("&ᴄᴄʟᴏꜱᴇ"));
        close.setItemMeta(meta);
        inv.setItem(26, close);
        
        p.openInventory(inv);
    }
    
    private void addShopItem(Inventory inv, int slot, Material mat, String name, int price) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.format(name));
        meta.setLore(Arrays.asList(
            TextUtils.format("&7ᴘʀɪᴄᴇ: &6⍟&ᴇ" + price),
            TextUtils.format("&8ᴄʟɪᴄᴋ ᴛᴏ ʙᴜʏ")
        ));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains(TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀ ꜱʜᴏᴘ"))) return;
        e.setCancelled(true);
        
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
        if (clicked.getType() == Material.BARRIER) {
            p.closeInventory();
            return;
        }
        
        // Get price from lore
        if (!clicked.hasItemMeta() || !clicked.getItemMeta().hasLore()) return;
        String lore = clicked.getItemMeta().getLore().get(0);
        String priceStr = lore.replaceAll("[^0-9]", "");
        if (priceStr.isEmpty()) return;
        int price = Integer.parseInt(priceStr);
        
        if (StellarCore.getInstance().getEconomyManager().hasEnough(p, price)) {
            StellarCore.getInstance().getEconomyManager().withdraw(p, price);
            p.getInventory().addItem(new ItemStack(clicked.getType(), 1));
            p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʙᴏᴜɢʜᴛ " + clicked.getType().name().toLowerCase() + " &ᴀꜰᴏʀ " + 
                StellarCore.getInstance().getEconomyManager().formatCurrency(price)));
        } else {
            p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜰᴜɴᴅꜱ!"));
        }
    }
}
