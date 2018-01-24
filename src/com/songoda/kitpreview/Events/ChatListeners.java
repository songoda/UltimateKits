package com.songoda.kitpreview.Events;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.Editor;
import com.songoda.kitpreview.Kits.Keys;
import com.songoda.kitpreview.Kits.Kit;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class ChatListeners implements Listener {

    KitPreview plugin = KitPreview.pl();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        try {
            final Player p = e.getPlayer();
            if (plugin.inEditor.containsKey(p)) {
                if (plugin.inEditor.get(p) == "price") {
                    e.setCancelled(true);
                    String msg = e.getMessage();
                    Kit kit = plugin.editingKit.get(p);

                    if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
                        p.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&8You must have &aVault &8installed to utilize economy.."));
                    } else {
                        if (!Arconix.pl().doMath().isNumeric(msg)) {
                            p.sendMessage(Arconix.pl().format().formatText("&a" + msg + " &8is not a number. Please do not include a &a$&8."));
                        } else {
                            if (plugin.getConfig().getString("data.kit." + kit.name + ".link") != null) {
                                plugin.getConfig().set("data.kit." + kit.name + ".link", null);
                                p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8LINK has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                            }
                            Double eco = Double.parseDouble(msg);
                            plugin.getConfig().set("data.kit." + kit.name + ".eco", eco);
                            p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8ECO &a" + Arconix.pl().format().formatEconomy(eco) + " &8assigned to &8Kit &9" + kit.name + "&8."));
                            plugin.holo.updateHolograms(true);
                            plugin.saveConfig();
                        }
                    }
                    plugin.inEditor.remove(p);
                } else if (plugin.inEditor.get(p) == "link") {
                    e.setCancelled(true);
                    String msg = e.getMessage();
                    Kit kit = plugin.editingKit.get(p);

                    if (plugin.getConfig().getString("data.kit." + kit.name + ".eco") != null) {
                        plugin.getConfig().set("data.kit." + kit.name + ".eco", null);
                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8ECO has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                    }
                    String link = msg;
                    plugin.getConfig().set("data.kit." + kit.name + ".link", link);
                    plugin.saveConfig();
                    p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Link &a" + link + " &8assigned to &8Kit &9" + kit.name + "&8."));
                    plugin.holo.updateHolograms(true);
                    plugin.inEditor.remove(p);
                } else if (plugin.inEditor.get(p) == "title") {
                    e.setCancelled(true);
                    String msg = e.getMessage();
                    Kit kit = plugin.editingKit.get(p);

                    plugin.getConfig().set("data.kit." + kit.name + ".title", msg);
                    plugin.saveConfig();
                    plugin.holo.updateHolograms(true);
                    p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Title &5" + msg + "&8 added to Kit &a" + kit.name + "&8."));
                    plugin.inEditor.remove(p);
                } else if (plugin.inEditor.get(p) == "command") {
                    e.setCancelled(true);
                    String msg = e.getMessage();
                    Editor edit = new Editor(plugin.editingKit.get(p).name, p);

                    ItemStack parseStack = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = parseStack.getItemMeta();

                    ArrayList<String> lore = new ArrayList<>();


                    int index = 0;
                    while (index < msg.length()) {
                        lore.add("§a/" + msg.substring(index, Math.min(index + 30, msg.length())));
                        index += 30;
                    }
                    meta.setLore(lore);
                    meta.setDisplayName(Lang.COMMAND.getConfigValue());
                    parseStack.setItemMeta(meta);


                    p.getInventory().addItem(parseStack);

                    p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Command &5" + msg + "&8 saved to your inventory."));
                    plugin.inEditor.remove(p);
                    edit.open(false);
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler()
    public void onCommandPreprocess(PlayerChatEvent event) {
        try {
            if (event.getMessage().equalsIgnoreCase("/kits")) {
                event.setCancelled(true);
            }
            if (event.getMessage().equalsIgnoreCase("/kit")) {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

}
