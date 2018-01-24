package com.songoda.kitpreview.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Created by songoda on 2/24/2017.
 */
public class ParticleHandler {

    KitPreview plugin = KitPreview.pl();
    public boolean v1_7 = plugin.getServer().getClass().getPackage().getName().contains("1_7");
    public boolean v1_8 = plugin.getServer().getClass().getPackage().getName().contains("1_8");


    public ParticleHandler() {
        checkDefaults();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                applyParticles();
            }
        }, 5L, 10L);
    }

    @SuppressWarnings("all")
    public void applyParticles() {
        try {
            if (plugin.getConfig().getString("data.particles") != null) {
                int amt = plugin.getConfig().getInt("data.particlesettings.ammount");
                String type = "";
                type = plugin.getConfig().getString("data.particlesettings.type");

                ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.particles");
                for (String loc : section.getKeys(false)) {
                    String str[] = loc.split(":");
                    String worldName = str[1].substring(0, str[1].length() - 1);
                    if (Bukkit.getServer().getWorld(worldName) != null) {
                        Location location = Arconix.pl().serialize().unserializeLocation(loc);
                        location.add(.5,0,.5);
                    /*
                    String typ = "";
                    if (plugin.getConfig().getString("data.type." + loc) != null) {
                        typ = plugin.getConfig().getString("data.type." + loc);
                    }

                    if (typ.equals("daily")) {
                        String kit = plugin.getConfig().getString("data.block." + loc);
                        for (Player p : plugin.getServer().getOnlinePlayers()) {
                            if (plugin.hoooks.isReady(p, kit)) {
                                if (v1_8 || v1_7) {
                                    //p.playEffect(location, org.bukkit.Effect.valueOf(type), 1, 0, (float) 0.25, (float) 0.25, (float) 0.25, 1, amt, 100);
                                } else {
                                    p.spawnParticle(org.bukkit.Particle.valueOf(type), location, amt, 0.25, 0.25, 0.25);
                                }
                            } else {
                                p.sendBlockChange(location, Material.AIR, (byte) 0);
                            }
                        }
                    } else { */
                        if (v1_8 || v1_7) {
                            location.getWorld().spigot().playEffect(location, org.bukkit.Effect.valueOf(type), 1, 0, (float) 0.25, (float) 0.25, (float) 0.25, 1, amt, 100);
                        } else {
                            location.getWorld().spawnParticle(org.bukkit.Particle.valueOf(type), location, amt, 0.25, 0.25, 0.25);
                        }
                        //}
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void checkDefaults() {
        try {
            if (plugin.getConfig().getInt("data.particlesettings.ammount") == 0) {
                plugin.getConfig().set("data.particlesettings.ammount", 25);
                plugin.saveConfig();
            }
            if (plugin.getConfig().getString("data.particlesettings.type") == null) {
                if (v1_7 || v1_8) {
                    plugin.getConfig().set("data.particlesettings.type", "WITCH_MAGIC");
                } else {
                    plugin.getConfig().set("data.particlesettings.type", "SPELL_WITCH");
                }
                plugin.saveConfig();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
