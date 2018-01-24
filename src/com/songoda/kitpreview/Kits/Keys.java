package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 2/24/2017.
 */
public class Keys {

    public static ItemStack spawnKey(String kit, int amt, int type) {
        ItemStack is = null;
        try {
            is = new ItemStack(Material.TRIPWIRE_HOOK, amt);
            kit = StringUtils.capitalize(kit);
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName(Arconix.pl().format().formatText(Lang.KEY_TITLE.getConfigValue(kit)));

            String title = "&eRegular";
            if (type == 2) {
                title = "&5Ultra";
            }
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(Arconix.pl().format().formatText(title + " &fKey"));
            lore.add(Arconix.pl().format().formatText(Lang.KEY_DESC1.getConfigValue(kit)));
            if (type == 2) {
                lore.add(Arconix.pl().format().formatText(Lang.KEY_DESC2.getConfigValue()));
            } else {
                lore.add(Arconix.pl().format().formatText(Lang.KEY_DESC3.getConfigValue()));
            }
            meta.setLore(lore);

            is.setItemMeta(meta);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

        return is;
    }

}
