package com.stellar.stellarcore.utils;

import org.bukkit.ChatColor;

public class TextUtils {
    
    private static final String NORMAL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String SMALLCAPS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡxʏᴢᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡxʏᴢ𝟶𝟷𝟸𝟹𝟺𝟻𝟼𝟽𝟾𝟿";
    
    public static String toSmallCaps(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = NORMAL.indexOf(c);
            if (index >= 0) {
                result.append(SMALLCAPS.charAt(index));
            } else {
                result.append(c);
            }
        }
        return result;
    }
    
    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', toSmallCaps(message));
    }
    
    public static String getPrefix() {
        return format("&8[&6⍟&8]&7 ");
    }
}
