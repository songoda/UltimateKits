package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 3/3/2017.
 */
public class BlockEditor {


    KitPreview plugin = KitPreview.pl();

    Location location = null;
    String loc = null;

    Kit kit = null;
    Block b = null;

    Player p = null;

    public BlockEditor(Block bl, Player pl) {
        try {
            String loco = Arconix.pl().serialize().serializeLocation(bl);
            location = bl.getLocation();
            loc = loco;
            kit = new Kit(loco);
            p = pl;
            b = bl;
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            plugin.inEditor.put(p, window);
            plugin.editing.put(p, b);
            plugin.editingKit.put(p, kit);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatText("&8This contains &a" + Arconix.pl().format().formatTitle(kit.name)));

            Methods.fillGlass(i);

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&5&lSwitch kit type"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to swap this kits type."));
            lore.add("");
            if (plugin.getConfig().getString("data.type." + loc) == null) {
                lore.add(Arconix.pl().format().formatText("&6Normal"));
                lore.add(Arconix.pl().format().formatText("&7Crate"));
                lore.add(Arconix.pl().format().formatText("&7Daily"));
            } else if (plugin.getConfig().getString("data.type." + loc).equals("crate")) {
                lore.add(Arconix.pl().format().formatText("&7Normal"));
                lore.add(Arconix.pl().format().formatText("&6Crate"));
                lore.add(Arconix.pl().format().formatText("&7Daily"));
            } else if (plugin.getConfig().getString("data.type." + loc).equals("daily")) {
                lore.add(Arconix.pl().format().formatText("&7Normal"));
                lore.add(Arconix.pl().format().formatText("&7Crate"));
                lore.add(Arconix.pl().format().formatText("&6Daily"));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lDecor Options"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit the decoration"));
            lore.add(Arconix.pl().format().formatText("&7options for this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lEdit kit"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit the kit"));
            lore.add(Arconix.pl().format().formatText("&7contained in this block."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("menu");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void decor() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatText("&8Editing decor for &a" + Arconix.pl().format().formatTitle(kit.name) + "&8."));

            Methods.fillGlass(i);

            ItemStack exit = new ItemStack(Material.valueOf(plugin.getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle Holograms"));
            ArrayList<String> lore = new ArrayList<>();
            if (plugin.getConfig().getString("data.holo." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle Particles"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getString("data.particles." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle DisplayItems"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getString("data.displayitems." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            if (plugin.getConfig().getItemStack("data.kit." + kit.name + ".displayitem") != null) {
                alli = plugin.getConfig().getItemStack("data.kit." + kit.name + ".displayitem");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lSet single DisplayItem"));
            lore = new ArrayList<>();
            if (plugin.getConfig().getItemStack("data.kit." + kit.name + ".displayitem") != null) {
                ItemStack is = plugin.getConfig().getItemStack("data.kit." + kit.name + ".displayitem");
                lore.add(Arconix.pl().format().formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Set a stagnant display item"));
            lore.add(Arconix.pl().format().formatText("&7for this kit to the item"));
            lore.add(Arconix.pl().format().formatText("&7in your hand."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Click with nothing in your"));
            lore.add(Arconix.pl().format().formatText("&7hand to reset."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            p.openInventory(i);
            defineInstance("decor");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void toggleHologram() {
        try {
            if (plugin.getConfig().getString("data.holo." + loc) == null) {
                plugin.getConfig().set("data.holo." + loc, true);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Hologram enabled for &a" + b.getType().toString() + "&8."));
            } else {
                plugin.getConfig().set("data.holo." + loc, null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Hologram disabled for &a" + b.getType().toString() + "&8."));
            }
            plugin.holo.updateHolograms(false);
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleParticles() {
        try {
            if (plugin.getConfig().getString("data.particles." + loc) == null) {
                plugin.getConfig().set("data.particles." + loc, true);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Particles enabled for &a" + b.getType().toString() + "&8."));
            } else {
                plugin.getConfig().set("data.particles." + loc, null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Particles disabled for &a" + b.getType().toString() + "&8."));
            }
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleDisplayItems() {
        try {
            if (plugin.getConfig().getString("data.displayitems." + loc) == null) {
                plugin.getConfig().set("data.displayitems." + loc, true);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Item Display enabled for &a" + b.getType().toString() + "&8."));
            } else {
                plugin.getConfig().set("data.displayitems." + loc, null);
                plugin.saveConfig();
                kit.removeDisplayItems();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Item Display disabled for &a" + b.getType().toString() + "&8."));
            }
            plugin.holo.updateHolograms(true);
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setDisplayItem() {
        try {
            if (p.getItemInHand().getType() != Material.AIR) {
                ItemStack is = p.getItemInHand();
                plugin.getConfig().set("data.kit." + kit.name + ".displayitem", is);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Custom Item Display set for kit &a" + kit.name + "&8."));
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8You will need to toggle on display items for this kit if you have not already."));
            } else {
                plugin.getConfig().set("data.kit." + kit.name + ".displayitem", null);
                plugin.saveConfig();
                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Custom Item Display removed from kit &a" + kit.name + "&8."));
            }
            p.closeInventory();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void changeDisplayType() {
        try {
            if (plugin.getConfig().getString("data.type." + loc) == null) {
                plugin.getConfig().set("data.type." + loc, "crate");
            } else if (plugin.getConfig().getString("data.type." + loc).equals("crate")) {
                plugin.getConfig().set("data.type." + loc, "daily");
            } else if (plugin.getConfig().getString("data.type." + loc).equals("daily")) {
                plugin.getConfig().set("data.type." + loc, null);
            }
            plugin.saveConfig();
            plugin.holo.updateHolograms(true);
            p.closeInventory();
            open();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
