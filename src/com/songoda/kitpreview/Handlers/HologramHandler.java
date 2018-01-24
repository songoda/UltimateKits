package com.songoda.kitpreview.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class HologramHandler {

    KitPreview plugin = KitPreview.pl();
    public boolean v1_7 = plugin.getServer().getClass().getPackage().getName().contains("1_7");


    public HologramHandler() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> updateHolograms(false), 5000L, 5000L);
        updateHolograms(true);
    }

    @SuppressWarnings("all")
    public void updateHolograms(boolean full) {
        try {
            if (plugin.getConfig().getString("data.block") != null && v1_7 == false) {
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.block");
                for (String loc : section.getKeys(false)) {
                    String str[] = loc.split(":");
                    String worldName = str[1].substring(0, str[1].length() - 1);
                    if (Bukkit.getServer().getWorld(worldName) != null) {
                        double multi = 0;
                        Location location = Arconix.pl().serialize().unserializeLocation(loc);
                        location.add(.5,.95,.5);
                        Block b = location.getBlock();

                        boolean notChest = false;

                        if (b.getType() != Material.TRAPPED_CHEST && b.getType() != Material.CHEST && b.getType() != Material.SIGN && b.getType() != Material.ENDER_CHEST) {
                            notChest = true;
                        }

                        if (plugin.getConfig().getString("data.displayitems." + loc) != null) {
                            if (notChest) {
                                multi = .75;
                            } else {
                                multi = .50;
                            }
                        } else if (notChest) {
                           multi += .30;
                        }

                        String kit = plugin.getConfig().getString("data.block." + loc);
                        if (plugin.getConfig().getString("data.kit." + kit + ".link") != null || plugin.getConfig().getString("data.kit." + kit + ".eco") != null) {
                            multi += .25;
                        }

                        location.add(0, multi, 0);

                        String kitn = plugin.getConfig().getString("data.block." + loc);

                        String type = "";
                        if (plugin.getConfig().getString("data.type." + loc) != null) {
                            type = plugin.getConfig().getString("data.type." + loc);
                        }

                        // compatability
                        boolean yes = false;
                        double radius = 2D;
                        List<Entity> near = location.getWorld().getEntities();
                        for (Entity e : near) {
                            if (e.getLocation().distance(location) <= radius) {
                                if (EntityType.ARMOR_STAND == e.getType()) {
                                    if (full == false) {
                                        yes = true;
                                    }
                                    if (plugin.getConfig().getString("data.holo." + loc) == null || full) {
                                        e.remove();
                                    }
                                }
                            }
                        }
                        // end compatability

                        if (plugin.getConfig().getString("data.holo." + loc) == null) {
                            Location loco = location.clone();
                            remove(loco);
                        } else {
                            if (yes == false) {
                                List<String> lines = new ArrayList<String>();

                                if (plugin.getConfig().getString("data.kit." + kit + ".title") == null) {
                                    lines.add("§5" + kitn);
                                } else {
                                    String title = plugin.getConfig().getString("data.kit." + kit + ".title");
                                    lines.add("§5" + Arconix.pl().format().formatText(title));
                                }

                                if (type.equals("daily")) {
                                    lines.add(Arconix.pl().format().formatText(Lang.PREVIEW_HOLOGRAM.getConfigValue(kit)));
                                    lines.add(Arconix.pl().format().formatText(Lang.DAILY_HOLOGRAM.getConfigValue(kit)));
                                } else {
                                    if (plugin.getConfig().getString("data.kit." + kit + ".link") == null && plugin.getConfig().getString("data.kit." + kit + ".eco") == null) {
                                        lines.add(Arconix.pl().format().formatText(Lang.PREVIEW_ONLY_HOLOGRAM.getConfigValue(kit)));
                                    } else {
                                        lines.add(Arconix.pl().format().formatText(Lang.PREVIEW_HOLOGRAM.getConfigValue(kit)));
                                    }

                                    if (type.equals("crate")) {
                                        lines.add(Arconix.pl().format().formatText(Lang.OPEN_CRATE_HOLOGRAM.getConfigValue()));
                                    } else {
                                        if (plugin.getConfig().getString("data.kit." + kit + ".link") != null || plugin.getConfig().getString("data.kit." + kit + ".eco") != null) {
                                            if (plugin.getConfig().getString("data.kit." + kit + ".eco") == null) {
                                                lines.add(Arconix.pl().format().formatText(Lang.BUY_LINK_HOLOGRAM.getConfigValue()));
                                            } else {
                                                double cost = plugin.getConfig().getDouble("data.kit." + kit + ".eco");
                                                if (cost != 0) {
                                                    lines.add(Arconix.pl().format().formatText(Lang.BUY_ECO_HOLOGRAM.getConfigValue(Arconix.pl().format().formatEconomy(cost))));
                                                } else {
                                                    lines.add(Lang.BUY_ECO_HOLOGRAM.getConfigValue(Arconix.pl().format().formatText(Lang.FREE.getConfigValue())));
                                                }
                                            }
                                        }
                                    }
                                }
                                remove(location);


                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    Arconix.pl().packetLibrary.getHologramManager().spawnHolograms(location, lines);
                                }, 5L);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void remove(Location location) {
        Location loco = location.clone();
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco);
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.add(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.add(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
        Arconix.pl().packetLibrary.getHologramManager().despawnHologram(loco.subtract(0, .25, 0));
    }
}
