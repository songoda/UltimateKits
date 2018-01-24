package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 2/24/2017.
 */
public class Buy {


    public static void confirmBuy(String kit, Player p) {
        try {
            KitPreview plugin = KitPreview.pl();
            String cost = Double.toString(plugin.getConfig().getDouble("data.kit." + kit + ".eco"));

            if (plugin.hooks.hasPermission(p, kit) && plugin.getConfig().getBoolean("Kits-Free-With-Perms")) {
                cost = "0";
            }
            Inventory i = Bukkit.createInventory(null, 45, Arconix.pl().format().formatTitle(Lang.GUI_TITLE_YESNO.getConfigValue(cost)));

            String title = "§c" + StringUtils.capitalize(kit.toLowerCase());
            ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
            if (plugin.getConfig().getItemStack("data.kit." + kit + ".displayitemkits") != null) {
                item = plugin.getConfig().getItemStack("data.kit." + kit + ".displayitemkits").clone();
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(title);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§a$" + Arconix.pl().format().formatEconomy(Double.parseDouble(cost)));


            int nu = 0;
            while (nu != 45) {
                i.setItem(nu, Methods.getGlass());
                nu++;
            }

            ItemStack item2 = new ItemStack(Material.valueOf(plugin.getConfig().getString("Buy-Icon")), 1);
            ItemMeta itemmeta2 = item2.getItemMeta();
            itemmeta2.setDisplayName(Lang.YES_GUI.getConfigValue());
            item2.setItemMeta(itemmeta2);

            ItemStack item3 = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta itemmeta3 = item3.getItemMeta();
            itemmeta3.setDisplayName(Lang.NO_GUI.getConfigValue());
            item3.setItemMeta(itemmeta3);

            i.setItem(13, item);
            i.setItem(29, item2);
            i.setItem(33, item3);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> p.openInventory(i), 1);
            plugin.buy.put(p, kit);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (plugin.buy.containsKey(p)) {
                    p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + Lang.TIMEOUT.getConfigValue()));
                    plugin.buy.remove(p);
                    p.closeInventory();
                }
            }, 200L);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}
