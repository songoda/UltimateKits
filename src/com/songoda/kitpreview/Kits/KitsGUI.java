package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by songoda on 3/16/2017.
 */
public class KitsGUI {

    public static void show(Player p, int page) {
        try {
            KitPreview plugin = KitPreview.pl();
            plugin.page.put(p, page);
            plugin.kits.clear();


            KitPreview.pl().xyz2.add(p.getUniqueId());
            String guititle = Lang.KITS_TITLE.getConfigValue();

            List<String> kits = plugin.hooks.getKits();


            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            List<String> kitList = new ArrayList<>();

            int ino = 14;
            if (plugin.getConfig().getBoolean("glassless"))
                ino = 54;
            int num = 0;
            int start = (page - 1) * ino;
            int show = 1;
            for (String kitItem : kits) {
                if (plugin.getConfig().getString("data.kit." + kitItem + ".blacklisted") == null) {
                    if (!plugin.getConfig().getBoolean("Only-Show-Kits-With-Perms") || plugin.hooks.hasPermission(p, kitItem)) {
                        if (num >= start) {
                            if (show <= ino) {
                                kitList.add(kitItem);
                                show++;
                            }
                        }
                    }
                }
                num++;
            }

            int n = 7;
            if (plugin.getConfig().getBoolean("glassless"))
                n = 9;
            int max = 27;
            if (kitList.size() > n) {
                max = 36;
            }
            if (plugin.getConfig().getBoolean("glassless")) {
                if (kitList.size() > n + 36)
                    max = max + 36;
                else if (kitList.size() > n + 27)
                    max = max + 27;
                else if (kitList.size() > n + 18)
                    max = max + 18;
                else if (kitList.size() > n + 9)
                    max = max + 9;
            }
            if (plugin.getConfig().getBoolean("glassless"))
                max = max - 18;
            Inventory i = Bukkit.createInventory(null, max, Arconix.pl().format().formatTitle(guititle));

            if (!plugin.getConfig().getBoolean("glassless")) {
                num = 0;
                while (num != max) {
                    ItemStack glass = Methods.getGlass();
                    i.setItem(num, glass);
                    num++;
                }
            }

            num = 10;
            if (plugin.getConfig().getBoolean("glassless"))
                num = 0;
            int id = 0;
            int tmax = max;
            if (!plugin.getConfig().getBoolean("glassless"))
                tmax = tmax - 10;
            while (num != tmax) {
                if (!plugin.getConfig().getBoolean("glassless")) {
                    if (num == 17)
                        num = 19;
                }
                if (id <= kitList.size() - 1) {
                    String kitItem = kitList.get(id);
                        String title = Lang.GUI_KIT_NAME.getConfigValue(Arconix.pl().format().formatText(kitItem, true));
                        if (plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".title") != null)
                            title = Arconix.pl().format().formatText(plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".title"));
                        plugin.kits.put(title, kitItem.toLowerCase());

                        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                        if (plugin.getConfig().getItemStack("data.kit." + kitItem.toLowerCase() + ".displayitemkits") != null)
                            item = plugin.getConfig().getItemStack("data.kit." + kitItem.toLowerCase() + ".displayitemkits").clone();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(title);
                        ArrayList<String> lore = new ArrayList<>();
                        if (plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".eco") != null) {
                            Double cost = plugin.getConfig().getDouble("data.kit." + kitItem.toLowerCase() + ".eco");
                            lore.add(Arconix.pl().format().formatText("&7This kit costs &a$" + Arconix.pl().format().formatEconomy(cost) + "&7."));
                        } else if (plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".link") != null)
                            lore.add(Lang.LINK.getConfigValue());


                        if (!Lang.ABOUT_KIT.getConfigValue().trim().equals("")) {
                            String[] parts = Lang.ABOUT_KIT.getConfigValue().split("\\|");
                            lore.add("");
                            for (String line : parts)
                                lore.add(Arconix.pl().format().formatText(line));
                        }
                        if (plugin.hooks.hasPermission(p, kitItem)) {
                            long time = new GregorianCalendar().getTimeInMillis();

                            long delay = plugin.hooks.getNextUse(kitItem, p) - time; // gets delay
                            if (delay >= 0) {
                                if (!Lang.PLEASE_WAIT.getConfigValue().trim().equals("")) {
                                    lore.add(Arconix.pl().format().formatText(Lang.PLEASE_WAIT.getConfigValue(Arconix.pl().format().readableTime(delay))));
                                }
                            } else {
                                if (!Lang.READY.getConfigValue().trim().equals("")) {
                                    lore.add(Arconix.pl().format().formatText(Lang.READY.getConfigValue()));
                                }
                            }
                        } else
                            lore.add(Arconix.pl().format().formatText(Lang.NO_ACCESS.getConfigValue()));
                        lore.add("");
                        lore.add(Arconix.pl().format().formatText(Lang.LEFT_PREVIEW.getConfigValue()));
                        if (plugin.hooks.hasPermission(p, kitItem)) {
                            lore.add(Arconix.pl().format().formatText(Lang.RIGHT_CLAIM.getConfigValue()));
                        } else if (plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".eco") != null || plugin.getConfig().getString("data.kit." + kitItem.toLowerCase() + ".link") != null) {
                            lore.add(Arconix.pl().format().formatText(Lang.RIGHT_BUY.getConfigValue()));
                        }

                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        i.setItem(num, item);
                } else {
                    i.setItem(num, new ItemStack(Material.AIR));
                }
                id++;
                num++;
            }

            ItemStack info = new ItemStack(Material.BOOK, 1);
            ItemMeta infometa = info.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            String[] parts = Lang.DETAILS.getConfigValue(p.getName()).split("\\|");
            boolean hit = false;
            for (String line : parts) {
                if (!hit)
                    infometa.setDisplayName(Arconix.pl().format().formatText(line));
                else
                    lore.add(Arconix.pl().format().formatText(line));
                hit = true;
            }
            infometa.setLore(lore);
            info.setItemMeta(infometa);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull = head;
            if (!plugin.v1_7)
                skull = Arconix.pl().getGUI().addTexture(head, "http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b");
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (plugin.v1_7)
                skullMeta.setOwner("MHF_ArrowRight");
            skull.setDurability((short) 3);
            skullMeta.setDisplayName(Lang.NEXT.getConfigValue());
            skull.setItemMeta(skullMeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull2 = head2;
            if (!plugin.v1_7)
                skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
            if (plugin.v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            skull2.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.LAST.getConfigValue());
            skull2.setItemMeta(skull2Meta);

            if (!plugin.getConfig().getBoolean("glassless"))
                i.setItem(max - 5, exit);
                        if (kitList.size() == 14)
                            i.setItem(max - 4, skull);
                        if (page != 1)
                            i.setItem(max - 6, skull2);
                        if (!plugin.getConfig().getBoolean("glassless"))
                            i.setItem(4, info);
            p.openInventory(i);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

}
