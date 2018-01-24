package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by songoda on 3/2/2017.
 */
public class Editor {


    KitPreview plugin = KitPreview.pl();

    Kit kit = null;

    Player p = null;

    public Editor(String kitt, Player pl) {
        try {
            kit = new Kit(null, kitt.toLowerCase());
            p = pl;
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            plugin.inEditor.put(p, window);
            plugin.editingKit.put(p, kit);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open(boolean backb) {
        try {
            Inventory i = Bukkit.createInventory(null, 54, Arconix.pl().format().formatTitle("&8You are editing kit: &9" + kit.name + "&8."));

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!plugin.v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (plugin.v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);


            ItemStack it = new ItemStack(Material.CHEST, 1);
            ItemMeta itmeta = it.getItemMeta();
            itmeta.setDisplayName(Arconix.pl().format().formatText("&5&l" + kit.name));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&fPermissions:"));
            lore.add(Arconix.pl().format().formatText("&7essentials.kits." + kit.dname.toLowerCase()));
            itmeta.setLore(lore);
            it.setItemMeta(itmeta);

            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta glassmeta = glass.getItemMeta();
            glassmeta.setDisplayName("§" + kit.name.replaceAll(".(?!$)", "$0§"));
            glass.setItemMeta(glassmeta);

            int num = 0;
            while (num != 9) {
                i.setItem(num, Methods.getGlass());
                num++;
            }
            int num2 = 54 - 9;
            while (num2 != 54) {
                i.setItem(num2, Methods.getGlass());
                num2++;
            }
            if (backb)
                i.setItem(0, back);
            i.setItem(4, it);
            i.setItem(8, exit);


            List<ItemStack> list = plugin.hooks.getItems(p, kit.dname, true);
            for (ItemStack is : list) {
                if (is.getAmount() > 64) {
                    int overflow = is.getAmount() % 64;
                    int stackamt = (int) ((long) (is.getAmount() / 64));
                    int num3 = 0;
                    while (num3 != stackamt) {
                        ItemStack is2 = is;
                        is2.setAmount(64);
                        i.setItem(num, is2);
                        num++;
                        num3++;
                    }
                    if (overflow != 0) {
                        ItemStack is2 = is;
                        is2.setAmount(overflow);
                        i.setItem(num, is2);
                        num++;
                    }

                } else {
                    i.setItem(num, is);
                    num++;
                }
            }

            ItemStack alli = new ItemStack(Material.EMERALD, 1);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9Selling Options"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit adjust"));
            lore.add(Arconix.pl().format().formatText("&7selling options."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(48, alli);

            alli = new ItemStack(Material.TRIPWIRE_HOOK, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&bKey Options"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit adjust"));
            lore.add(Arconix.pl().format().formatText("&7kit key options."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7You can give players a key"));
            lore.add(Arconix.pl().format().formatText("&7that they can use to receive"));
            lore.add(Arconix.pl().format().formatText("&7all or part of a kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(49, alli);

            alli = new ItemStack(Material.ITEM_FRAME, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&5GUI Options"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit GUI options"));
            lore.add(Arconix.pl().format().formatText("&7for this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(50, alli);

            alli = new ItemStack(Material.PAPER, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&fAdd Command"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to add a command"));
            lore.add(Arconix.pl().format().formatText("&7to this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(51, alli);

            alli = new ItemStack(Material.REDSTONE, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&aSave Changes"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to save all changes."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(52, alli);

            p.openInventory(i);
            defineInstance("editor");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void keys() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8Key Options for &a" + kit.name + "&8."));

            Methods.fillGlass(i);

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!plugin.v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (plugin.v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&c&lAdd a &e&lRegular &c&lkey to inventory"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7place a normal key into your"));
            lore.add(Arconix.pl().format().formatText("&7inventory."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7A regular key will follow the"));
            lore.add(Arconix.pl().format().formatText("&7options defined in this GUI."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Right clicking this will"));
            lore.add(Arconix.pl().format().formatText("&7result in a stack."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            int keyReward = kit.keyReward();

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&c&lAdd a &5&lUltra &c&lkey to inventory"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7place a ultra key into your"));
            lore.add(Arconix.pl().format().formatText("&7inventory."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7A ultra key will disregard the"));
            lore.add(Arconix.pl().format().formatText("&7options defined in this GUI"));
            lore.add(Arconix.pl().format().formatText("&7and reward the player with"));
            lore.add(Arconix.pl().format().formatText("&7all the items in the kit."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Right clicking this will"));
            lore.add(Arconix.pl().format().formatText("&7result in a stack."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lChange Reward"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Current reward amount: " + keyReward));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will allow"));
            lore.add(Arconix.pl().format().formatText("&7you to change the amount of"));
            lore.add(Arconix.pl().format().formatText("&7items a player will receive"));
            lore.add(Arconix.pl().format().formatText("&7when using a key on this kit."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Left clicking this will"));
            lore.add(Arconix.pl().format().formatText("&7increase the amount as"));
            lore.add(Arconix.pl().format().formatText("&7right clicking will decrease."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("keys");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void selling() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8Selling Options for &a" + kit.name + "&8."));

            Methods.fillGlass(i);

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!plugin.v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (plugin.v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&c&lSet not for sale"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7remove this kit from sale."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lSet kit link"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7allow you to set a link"));
            lore.add(Arconix.pl().format().formatText("&7that players will receive"));
            lore.add(Arconix.pl().format().formatText("&7when attempting to purchase"));
            lore.add(Arconix.pl().format().formatText("&7this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lSet kit price"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7allow you to set a price"));
            lore.add(Arconix.pl().format().formatText("&7that players will be able to"));
            lore.add(Arconix.pl().format().formatText("&7purchase this kit for"));
            lore.add(Arconix.pl().format().formatText("&7requires &aVault&7."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lToggle Delay"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getString("data.kit." + kit.dname + ".delay") != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Clicking this option will"));
            lore.add(Arconix.pl().format().formatText("&7allow you to enable or disable"));
            lore.add(Arconix.pl().format().formatText("&7the kits delay set in the"));
            lore.add(Arconix.pl().format().formatText("&7essentials config."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            p.openInventory(i);
            defineInstance("selling");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void gui() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8GUI Options for &a" + kit.name + "&8."));

            Methods.fillGlass(i);

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!plugin.v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (plugin.v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lSet Title"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Sets the kit title for holograms"));
            lore.add(Arconix.pl().format().formatText("&7and the kit / kits GUIs."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lRemove Title"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Resets the kit title for holograms"));
            lore.add(Arconix.pl().format().formatText("&7and the kit / kits GUIs."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            if (plugin.getConfig().getItemStack("data.kit." + kit.dname + ".displayitemkits") != null) {
                alli = plugin.getConfig().getItemStack("data.kit." + kit.dname + ".displayitemkits");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lSet /Kits DisplayItem"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getItemStack("data.kit." + kit.dname + ".displayitemkits") != null) {
                ItemStack is = plugin.getConfig().getItemStack("data.kit." + kit.dname + ".displayitemkits");
                lore.add(Arconix.pl().format().formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Click with nothing in your"));
            lore.add(Arconix.pl().format().formatText("&7hand to reset."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lBlacklist kit"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getString("data.kit." + kit.dname + ".blacklisted") != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aBlacklisted&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently not &cBlacklisted&7."));
            }
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7A blacklisted kit will not"));
            lore.add(Arconix.pl().format().formatText("&7show up in the /kits gui."));
            lore.add(Arconix.pl().format().formatText("&7This is usually optimal for"));
            lore.add(Arconix.pl().format().formatText("&7preventing players from seeing"));
            lore.add(Arconix.pl().format().formatText("&7non obtainable kits or starter kits."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            p.openInventory(i);
            defineInstance("gui");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setKitsDisplayItem() {
        try {
            if (p.getItemInHand().getType() != Material.AIR) {
                ItemStack is = p.getItemInHand();
                plugin.getConfig().set("data.kit." + kit.dname + ".displayitemkits", is);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Custom Item Display set for kit &a" + kit.name + "&8."));
            } else {
                plugin.getConfig().set("data.kit." + kit.dname + ".displayitemkits", null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Custom Item Display removed from kit &a" + kit.name + "&8."));
            }
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void createCommand() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (plugin.inEditor.containsKey(p)) {
                    if (plugin.inEditor.get(p) == "command") {
                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "Editing Timed out."));
                        plugin.inEditor.remove(p);
                    }
                }
            }, 500L);
            p.closeInventory();
            defineInstance("command");
            p.sendMessage("");
            p.sendMessage(Arconix.pl().format().formatText("Please type a command. Example: &aeco give {player} 1000"));
            p.sendMessage(Arconix.pl().format().formatText("do not include a &a/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void saveKit(Player p, Inventory i) {
        try {
            ItemStack[] items = i.getContents();
            int num = 0;
            while (num < 9) {
                items[num] = null;
                num++;
            }

            items = Arrays.copyOf(items, items.length - 9);

            plugin.hooks.saveKit(p, kit.dname, items);
            p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Changes to &a" + kit.name + " &8saved successfully."));
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void blacklist() {
        try {
            if (plugin.getConfig().getString("data.kit." + kit.dname + ".blacklisted") == null) {
                plugin.getConfig().set("data.kit." + kit.dname + ".blacklisted", true);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&a" + kit.name + "&8 has been blacklisted&8."));
            } else {
                plugin.getConfig().set("data.kit." + kit.dname + ".blacklisted", null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&a" + kit.name + "&8 has been unblacklisted&8."));
            }
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleDelay() {
        try {
            if (plugin.getConfig().getString("data.kit." + kit.dname + ".delay") == null) {
                plugin.getConfig().set("data.kit." + kit.dname + ".delay", true);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Delay enabled for &a" + kit.name + "&8."));
            } else {
                plugin.getConfig().set("data.kit." + kit.dname + ".delay", null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Delay disabled for &a" + kit.name + "&8."));
            }
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setNoSale() {
        try {
            plugin.getConfig().set("data.kit." + kit.dname + ".eco", null);
            plugin.getConfig().set("data.kit." + kit.dname + ".link", null);
            plugin.saveConfig();
            p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&9Link and ECO removed from Kit &8" + kit.name + "&9."));
            plugin.holo.updateHolograms(true);
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setTitle() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (plugin.inEditor.containsKey(p)) {
                    if (plugin.inEditor.get(p) == "title") {
                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "Editing Timed out."));
                        plugin.inEditor.remove(p);
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("title");
            p.sendMessage("");
            p.sendMessage(Arconix.pl().format().formatText("Type a title for the GUI. Example: &aThe Cool Kids Kit"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void RemoveTitle() {
        try {
            plugin.getConfig().set("data.kit." + kit.dname + ".title", null);
            plugin.saveConfig();
            plugin.holo.updateHolograms(true);
            p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Title removed from Kit &a" + kit.name + "&8's GUI."));
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editPrice() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (plugin.inEditor.containsKey(p)) {
                    if (plugin.inEditor.get(p) == "price") {
                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "Editing Timed out."));
                        plugin.inEditor.remove(p);
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("price");
            p.sendMessage("");
            p.sendMessage(Arconix.pl().format().formatText("Please type a price. Example: &a50000"));
            p.sendMessage(Arconix.pl().format().formatText("&cUse only numbers."));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editLink() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (plugin.inEditor.containsKey(p)) {
                    if (plugin.inEditor.get(p) == "link") {
                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "Editing Timed out."));
                        plugin.inEditor.remove(p);
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("link");
            p.sendMessage("");
            p.sendMessage(Arconix.pl().format().formatText("Please type a link. Example: &ahttp://buy.viscernity.com/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


}
