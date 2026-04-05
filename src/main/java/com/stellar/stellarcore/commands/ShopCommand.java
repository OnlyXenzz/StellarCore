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
import java.util.*;

public class ShopCommand implements CommandExecutor, Listener {
    
    private final Map<String, Map<Material, Integer>> categories = new HashMap<>();
    private final Map<UUID, String> playerCategory = new HashMap<>();
    
    public ShopCommand() {
        initCategories();
    }
    
    private void initCategories() {
        // === KATEGORI ORES & MINERALS ===
        Map<Material, Integer> ores = new HashMap<>();
        ores.put(Material.COAL, 5);
        ores.put(Material.IRON_INGOT, 10);
        ores.put(Material.COPPER_INGOT, 8);
        ores.put(Material.GOLD_INGOT, 20);
        ores.put(Material.LAPIS_LAZULI, 15);
        ores.put(Material.REDSTONE, 12);
        ores.put(Material.EMERALD, 50);
        ores.put(Material.DIAMOND, 100);
        ores.put(Material.NETHERITE_INGOT, 500);
        ores.put(Material.QUARTZ, 7);
        ores.put(Material.AMETHYST_SHARD, 25);
        ores.put(Material.ANCIENT_DEBRIS, 400);
        categories.put("ᴏʀᴇꜱ", ores);
        
        // === KATEGORI GEMS & SPECIAL ===
        Map<Material, Integer> gems = new HashMap<>();
        gems.put(Material.ENDER_PEARL, 30);
        gems.put(Material.ENDER_EYE, 50);
        gems.put(Material.SHULKER_SHELL, 200);
        gems.put(Material.NAUTILUS_SHELL, 80);
        gems.put(Material.HEART_OF_THE_SEA, 300);
        gems.put(Material.TOTEM_OF_UNDYING, 1000);
        gems.put(Material.ELYTRA, 1500);
        gems.put(Material.DRAGON_BREATH, 250);
        gems.put(Material.NETHER_STAR, 2000);
        gems.put(Material.BEACON, 500);
        gems.put(Material.CONDUIT, 400);
        categories.put("ɢᴇᴍꜱ & ꜱᴘᴇᴄɪᴀʟ", gems);
        
        // === KATEGORI FOOD ===
        Map<Material, Integer> food = new HashMap<>();
        food.put(Material.APPLE, 3);
        food.put(Material.GOLDEN_APPLE, 50);
        food.put(Material.ENCHANTED_GOLDEN_APPLE, 500);
        food.put(Material.BREAD, 5);
        food.put(Material.COOKED_BEEF, 8);
        food.put(Material.COOKED_PORKCHOP, 8);
        food.put(Material.COOKED_CHICKEN, 6);
        food.put(Material.COOKED_MUTTON, 7);
        food.put(Material.COOKED_RABBIT, 7);
        food.put(Material.COOKED_COD, 5);
        food.put(Material.COOKED_SALMON, 6);
        food.put(Material.CAKE, 20);
        food.put(Material.PUMPKIN_PIE, 15);
        food.put(Material.COOKIE, 2);
        food.put(Material.HONEY_BOTTLE, 10);
        categories.put("ꜰᴏᴏᴅ", food);
        
        // === KATEGORI TOOLS & WEAPONS ===
        Map<Material, Integer> tools = new HashMap<>();
        tools.put(Material.WOODEN_PICKAXE, 10);
        tools.put(Material.STONE_PICKAXE, 20);
        tools.put(Material.IRON_PICKAXE, 50);
        tools.put(Material.GOLDEN_PICKAXE, 40);
        tools.put(Material.DIAMOND_PICKAXE, 200);
        tools.put(Material.NETHERITE_PICKAXE, 800);
        tools.put(Material.WOODEN_SWORD, 8);
        tools.put(Material.STONE_SWORD, 15);
        tools.put(Material.IRON_SWORD, 40);
        tools.put(Material.DIAMOND_SWORD, 150);
        tools.put(Material.NETHERITE_SWORD, 600);
        tools.put(Material.BOW, 50);
        tools.put(Material.CROSSBOW, 60);
        tools.put(Material.TRIDENT, 300);
        tools.put(Material.SHIELD, 25);
        categories.put("ᴛᴏᴏʟꜱ & ᴡᴇᴀᴘᴏɴꜱ", tools);
        
        // === KATEGORI ARMOR ===
        Map<Material, Integer> armor = new HashMap<>();
        armor.put(Material.LEATHER_HELMET, 30);
        armor.put(Material.LEATHER_CHESTPLATE, 50);
        armor.put(Material.LEATHER_LEGGINGS, 40);
        armor.put(Material.LEATHER_BOOTS, 25);
        armor.put(Material.IRON_HELMET, 80);
        armor.put(Material.IRON_CHESTPLATE, 120);
        armor.put(Material.IRON_LEGGINGS, 100);
        armor.put(Material.IRON_BOOTS, 70);
        armor.put(Material.DIAMOND_HELMET, 300);
        armor.put(Material.DIAMOND_CHESTPLATE, 500);
        armor.put(Material.DIAMOND_LEGGINGS, 400);
        armor.put(Material.DIAMOND_BOOTS, 250);
        armor.put(Material.NETHERITE_HELMET, 1200);
        armor.put(Material.NETHERITE_CHESTPLATE, 2000);
        armor.put(Material.NETHERITE_LEGGINGS, 1600);
        armor.put(Material.NETHERITE_BOOTS, 1000);
        categories.put("ᴀʀᴍᴏʀ", armor);
        
        // === KATEGORI BLOCKS ===
        Map<Material, Integer> blocks = new HashMap<>();
        blocks.put(Material.OAK_LOG, 4);
        blocks.put(Material.SPRUCE_LOG, 4);
        blocks.put(Material.BIRCH_LOG, 4);
        blocks.put(Material.JUNGLE_LOG, 4);
        blocks.put(Material.DARK_OAK_LOG, 4);
        blocks.put(Material.MANGROVE_LOG, 4);
        blocks.put(Material.CHERRY_LOG, 4);
        blocks.put(Material.COBBLESTONE, 2);
        blocks.put(Material.STONE, 3);
        blocks.put(Material.SAND, 2);
        blocks.put(Material.GRAVEL, 2);
        blocks.put(Material.OBSIDIAN, 15);
        blocks.put(Material.CRYING_OBSIDIAN, 20);
        blocks.put(Material.GLASS, 3);
        blocks.put(Material.BRICK, 8);
        blocks.put(Material.NETHER_BRICKS, 10);
        blocks.put(Material.END_STONE, 12);
        categories.put("ʙʟᴏᴄᴋꜱ", blocks);
        
        // === KATEGORI POTIONS ===
        Map<Material, Integer> potions = new HashMap<>();
        potions.put(Material.POTION, 20);
        potions.put(Material.SPLASH_POTION, 25);
        potions.put(Material.LINGERING_POTION, 30);
        potions.put(Material.EXPERIENCE_BOTTLE, 15);
        categories.put("ᴘᴏᴛɪᴏɴꜱ", potions);
        
        // === KATEGORI MOB DROPS ===
        Map<Material, Integer> mobDrops = new HashMap<>();
        mobDrops.put(Material.GUNPOWDER, 10);
        mobDrops.put(Material.ARROW, 2);
        mobDrops.put(Material.SPECTRAL_ARROW, 5);
        mobDrops.put(Material.TIPPED_ARROW, 8);
        mobDrops.put(Material.BONE, 5);
        mobDrops.put(Material.STRING, 8);
        mobDrops.put(Material.SPIDER_EYE, 6);
        mobDrops.put(Material.FERMENTED_SPIDER_EYE, 15);
        mobDrops.put(Material.BLAZE_ROD, 40);
        mobDrops.put(Material.GHAST_TEAR, 60);
        mobDrops.put(Material.MAGMA_CREAM, 20);
        mobDrops.put(Material.SLIME_BALL, 12);
        mobDrops.put(Material.PHANTOM_MEMBRANE, 50);
        mobDrops.put(Material.RABBIT_FOOT, 30);
        mobDrops.put(Material.SCUTE, 25);  // ← SUDAH DIPERBAIKI
        categories.put("ᴍᴏʙ ᴅʀᴏᴘꜱ", mobDrops);
        
        // === KATEGORI DYES ===
        Map<Material, Integer> dyes = new HashMap<>();
        dyes.put(Material.WHITE_DYE, 3);
        dyes.put(Material.BLACK_DYE, 3);
        dyes.put(Material.BLUE_DYE, 3);
        dyes.put(Material.BROWN_DYE, 3);
        dyes.put(Material.CYAN_DYE, 3);
        dyes.put(Material.GRAY_DYE, 3);
        dyes.put(Material.GREEN_DYE, 3);
        dyes.put(Material.LIGHT_BLUE_DYE, 3);
        dyes.put(Material.LIGHT_GRAY_DYE, 3);
        dyes.put(Material.LIME_DYE, 3);
        dyes.put(Material.MAGENTA_DYE, 3);
        dyes.put(Material.ORANGE_DYE, 3);
        dyes.put(Material.PINK_DYE, 3);
        dyes.put(Material.PURPLE_DYE, 3);
        dyes.put(Material.RED_DYE, 3);
        dyes.put(Material.YELLOW_DYE, 3);
        categories.put("ᴅʏᴇꜱ", dyes);
        
        // === KATEGORI RECORDS ===
        Map<Material, Integer> records = new HashMap<>();
        records.put(Material.MUSIC_DISC_13, 50);
        records.put(Material.MUSIC_DISC_CAT, 50);
        records.put(Material.MUSIC_DISC_BLOCKS, 50);
        records.put(Material.MUSIC_DISC_CHIRP, 50);
        records.put(Material.MUSIC_DISC_FAR, 50);
        records.put(Material.MUSIC_DISC_MALL, 50);
        records.put(Material.MUSIC_DISC_MELLOHI, 50);
        records.put(Material.MUSIC_DISC_STAL, 50);
        records.put(Material.MUSIC_DISC_STRAD, 50);
        records.put(Material.MUSIC_DISC_WARD, 50);
        records.put(Material.MUSIC_DISC_11, 50);
        records.put(Material.MUSIC_DISC_WAIT, 50);
        records.put(Material.MUSIC_DISC_PIGSTEP, 50);
        records.put(Material.MUSIC_DISC_OTHERSIDE, 50);
        records.put(Material.MUSIC_DISC_5, 50);
        records.put(Material.MUSIC_DISC_RELIC, 50);
        categories.put("ʀᴇᴄᴏʀᴅꜱ", records);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄᴏɴʟʏ ᴘʟᴀʏᴇʀꜱ ᴄᴀɴ ᴜꜱᴇ ᴛʜɪꜱ!"));
            return true;
        }
        
        Player p = (Player) sender;
        openCategoryMenu(p);
        return true;
    }
    
    private void openCategoryMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 36, TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀ ꜱʜᴏᴘ - ᴄᴀᴛᴇɢᴏʀʏ"));
        
        int slot = 0;
        for (String category : categories.keySet()) {
            ItemStack catItem = createCategoryItem(category);
            inv.setItem(slot, catItem);
            slot++;
        }
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(TextUtils.format("&ᴄᴄʟᴏꜱᴇ"));
        close.setItemMeta(closeMeta);
        inv.setItem(31, close);
        
        p.openInventory(inv);
    }
    
    private ItemStack createCategoryItem(String category) {
        Material material;
        
        // Pilih icon berdasarkan kategori
        switch (category) {
            case "ᴏʀᴇꜱ": material = Material.DIAMOND; break;
            case "ɢᴇᴍꜱ & ꜱᴘᴇᴄɪᴀʟ": material = Material.ENDER_EYE; break;
            case "ꜰᴏᴏᴅ": material = Material.COOKED_BEEF; break;
            case "ᴛᴏᴏʟꜱ & ᴡᴇᴀᴘᴏɴꜱ": material = Material.DIAMOND_SWORD; break;
            case "ᴀʀᴍᴏʀ": material = Material.DIAMOND_CHESTPLATE; break;
            case "ʙʟᴏᴄᴋꜱ": material = Material.STONE; break;
            case "ᴘᴏᴛɪᴏɴꜱ": material = Material.POTION; break;
            case "ᴍᴏʙ ᴅʀᴏᴘꜱ": material = Material.BONE; break;
            case "ᴅʏᴇꜱ": material = Material.RED_DYE; break;
            case "ʀᴇᴄᴏʀᴅꜱ": material = Material.MUSIC_DISC_CAT; break;
            default: material = Material.CHEST;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.format("&6&ʟ" + category));
        meta.setLore(Arrays.asList(
            TextUtils.format("&7ᴄʟɪᴄᴋ ᴛᴏ ᴠɪᴇᴡ ɪᴛᴇᴍꜱ ɪɴ ᴛʜɪꜱ ᴄᴀᴛᴇɢᴏʀʏ")
        ));
        item.setItemMeta(meta);
        return item;
    }
    
    private void openItemsMenu(Player p, String category) {
        Map<Material, Integer> items = categories.get(category);
        if (items == null) return;
        
        playerCategory.put(p.getUniqueId(), category);
        
        Inventory inv = Bukkit.createInventory(null, 54, TextUtils.format("&6&ʟ" + category + " &8- &7ᴘᴀɢᴇ 1"));
        
        int slot = 0;
        for (Map.Entry<Material, Integer> entry : items.entrySet()) {
            if (slot >= 45) break;
            inv.setItem(slot, createItemDisplay(entry.getKey(), entry.getValue()));
            slot++;
        }
        
        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(TextUtils.format("&ᴀʙᴀᴄᴋ ᴛᴏ ᴄᴀᴛᴇɢᴏʀɪᴇꜱ"));
        back.setItemMeta(backMeta);
        inv.setItem(45, back);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(TextUtils.format("&ᴄᴄʟᴏꜱᴇ"));
        close.setItemMeta(closeMeta);
        inv.setItem(49, close);
        
        p.openInventory(inv);
    }
    
    private ItemStack createItemDisplay(Material material, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String name = material.name().toLowerCase().replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
            }
        }
        name = capitalized.toString().trim();
        
        meta.setDisplayName(TextUtils.format("&ғ" + name));
        meta.setLore(Arrays.asList(
            TextUtils.format("&7ᴘʀɪᴄᴇ: &6⍟&ᴇ" + price),
            TextUtils.format("&8ᴄʟɪᴄᴋ ᴛᴏ ʙᴜʏ")
        ));
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();
        
        if (!title.contains(TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀ ꜱʜᴏᴘ"))) return;
        
        e.setCancelled(true);
        
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
        // === KATEGORI MENU ===
        if (title.equals(TextUtils.format("&6&ʟꜱᴛᴇʟʟᴀʀ ꜱʜᴏᴘ - ᴄᴀᴛᴇɢᴏʀʏ"))) {
            if (clicked.getType() == Material.BARRIER) {
                p.closeInventory();
                return;
            }
            
            String displayName = clicked.getItemMeta().getDisplayName();
            for (String category : categories.keySet()) {
                if (displayName.contains(TextUtils.format("&6&ʟ" + category))) {
                    openItemsMenu(p, category);
                    return;
                }
            }
            return;
        }
        
        // === ITEMS MENU ===
        if (clicked.getType() == Material.ARROW) {
            openCategoryMenu(p);
            return;
        }
        
        if (clicked.getType() == Material.BARRIER) {
            p.closeInventory();
            return;
        }
        
        // Buy item
        Material mat = clicked.getType();
        String currentCategory = playerCategory.get(p.getUniqueId());
        if (currentCategory != null && categories.containsKey(currentCategory)) {
            Map<Material, Integer> items = categories.get(currentCategory);
            if (items.containsKey(mat)) {
                int price = items.get(mat);
                
                if (StellarCore.getInstance().getEconomyManager().hasEnough(p, price)) {
                    StellarCore.getInstance().getEconomyManager().withdraw(p, price);
                    p.getInventory().addItem(new ItemStack(mat, 1));
                    p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴀʙᴏᴜɢʜᴛ &ғ" + mat.name().toLowerCase() + " &ᴀꜰᴏʀ " + 
                        StellarCore.getInstance().getEconomyManager().formatCurrency(price)));
                    
                    // Refresh menu
                    openItemsMenu(p, currentCategory);
                } else {
                    p.sendMessage(TextUtils.getPrefix() + TextUtils.format("&ᴄɪɴꜱᴜꜰꜰɪᴄɪᴇɴᴛ ꜱᴛᴇʟʟᴀʀ!"));
                }
            }
        }
    }
}
