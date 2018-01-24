package com.songoda.kitpreview.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class DisplayItemHandler {

    KitPreview plugin = KitPreview.pl();

    public DisplayItemHandler() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> displayItems(), 30L, 30L);
    }

    @SuppressWarnings("all")
    public static void displayItems() {
        try {
            KitPreview plugin = KitPreview.pl();
            if (plugin.getConfig().getString("data.block") != null) {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.block");
                for (String loc : section.getKeys(false)) {
                    if (plugin.getConfig().getString("data.displayitems." + loc) != null) {
                        String kit = plugin.getConfig().getString("data.block." + loc);

                        Location location = Arconix.pl().serialize().unserializeLocation(loc);
                        location.add(0.5,0,0.5);
                        boolean hit = false;

                        List<ItemStack> list = plugin.hooks.getItems(null, kit, false);
                        for (Entity e : location.getChunk().getEntities()) {
                            if (e.getType() == EntityType.DROPPED_ITEM) {
                                if (e.getLocation().getX() == location.getX() && e.getLocation().getZ() == location.getZ()) {
                                    hit = true;
                                    Item i = (Item) e;
                                    if (i.getItemStack().getItemMeta().getDisplayName() == null) {
                                        i.remove();
                                    } else {
                                        int inum = Integer.parseInt(i.getItemStack().getItemMeta().getDisplayName()) + 1;
                                        if (inum > list.size()) {
                                            inum = 1;
                                        }

                                        ItemStack is = list.get(inum - 1);
                                        if (plugin.getConfig().getItemStack("data.kit." + kit + ".displayitem") != null) {
                                            is = plugin.getConfig().getItemStack("data.kit." + kit + ".displayitem").clone();
                                        }
                                        ItemMeta meta = is.getItemMeta();
                                        is.setAmount(1);
                                        meta.setDisplayName(Integer.toString(inum));
                                        is.setItemMeta(meta);
                                        i.setItemStack(is);
                                        i.setPickupDelay(9999);
                                    }
                                }
                            }
                        }
                        if (hit == false) {
                            ItemStack is = list.get(0);
                            is.setAmount(1);
                            ItemMeta meta = is.getItemMeta();
                            meta.setDisplayName("0");
                            is.setItemMeta(meta);
                            Item i = location.getWorld().dropItem(location.add(0, 1, 0), list.get(0));
                            Vector vec = new Vector(0, 0, 0);
                            i.setVelocity(vec);
                            i.setPickupDelay(9999);
                            i.setMetadata("betterdrops_ignore", new FixedMetadataValue(plugin, true));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}
