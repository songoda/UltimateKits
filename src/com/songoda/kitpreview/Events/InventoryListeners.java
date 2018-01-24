package com.songoda.kitpreview.Events;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.*;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;

public class InventoryListeners implements Listener {

    KitPreview plugin = KitPreview.pl();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            Player p = (Player) event.getWhoClicked();
            if (plugin.buy.containsKey(p)) {
                if (event.getSlot() == 29) {
                    Kit kit = new Kit(null, plugin.buy.get(p));
                    kit.buyWithEconomy(p);
                    p.closeInventory();
                    plugin.buy.remove(p);
                } else if (event.getSlot() == 33) {
                    p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + Lang.BUYCANCELLED.getConfigValue()));
                    p.closeInventory();
                    plugin.buy.remove(p);
                }
                event.setCancelled(true);
            } else if (event.getInventory().getTitle().startsWith(Lang.KITS_TITLE.getConfigValue()) || plugin.xyz2.contains(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
                if (plugin.references.isPlaySound()) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), plugin.references.getSound(), 10.0F, 1.0F);
                }
                if (event.getAction() != InventoryAction.NOTHING) {
                    if (event.getCurrentItem().getType() != Material.AIR) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            ItemStack clicked = event.getCurrentItem();
                            int page = plugin.page.get(p);
                            if (event.getSlot() == 4) {
                            } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.NEXT.getConfigValue()))) {
                                KitsGUI.show(p, page + 1);
                            } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.LAST.getConfigValue()))) {
                                KitsGUI.show(p, page - 1);
                            } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                                p.closeInventory();
                                plugin.xyz2.remove(p.getUniqueId());
                            } else if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("")) {
                            } else {
                                if (clicked.getItemMeta().hasDisplayName()) {
                                    p.closeInventory();
                                    String kitName = plugin.kits.get(clicked.getItemMeta().getDisplayName());
                                    Kit kit = new Kit(null, kitName);
                                    if (event.getClick().isLeftClick()) {
                                        kit.display(p, true);
                                    } else {
                                        kit.buy(p);
                                    }
                                    plugin.xyz2.remove(p.getUniqueId());

                                }
                            }
                        }
                    }
                }
            } else if (event.getInventory().getTitle().startsWith(Lang.PREVIEW_TITLE.getConfigValue().replace("{KIT}", "")) || plugin.xyz.contains(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
                if (plugin.references.isPlaySound()) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), plugin.references.getSound(), 10.0F, 1.0F);
                }
                if (event.getAction() != InventoryAction.NOTHING) {
                    if (event.getCurrentItem().getType() != Material.AIR) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            ItemStack clicked = event.getCurrentItem();
                            if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BACK.getConfigValue()))) {
                                KitsGUI.show(p, 1);
                            }
                            if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                                p.closeInventory();
                            }
                            if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BUYNOW.getConfigValue()))) {
                                p.closeInventory();
                                String kitName = plugin.inKit.get(p).name;
                                Kit kit = new Kit(null, kitName);
                                kit.buy(p);
                            }
                        }
                    }
                }

                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, () -> p.updateInventory(), 5L);
            } else if (plugin.inEditor.containsKey(event.getWhoClicked()) && event.getClickedInventory() != null) {
                if (plugin.inEditor.get(event.getWhoClicked()) == "menu") {
                    event.setCancelled(true);
                    BlockEditor edit = new BlockEditor(plugin.editing.get(p), p);

                    if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        ItemStack clicked = event.getCurrentItem();
                        if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                            p.closeInventory();
                        }
                        if (event.getSlot() > 44 || event.getSlot() < 9) {
                            if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                                event.setCancelled(true);
                            }
                        }
                        if (event.getSlot() == 11) {
                            edit.changeDisplayType();
                        } else if (event.getSlot() == 13) {
                            edit.decor();
                        } else if (event.getSlot() == 15) {

                            Editor ed = new Editor(plugin.editingKit.get(p).name, p);
                            ed.open(true);
                        }
                    }
                } else if (plugin.inEditor.get(event.getWhoClicked()) == "editor") {
                    Editor edit = new Editor(plugin.editingKit.get(p).name, p);
                    if (event.getSlot() > 44 || event.getSlot() < 9) {
                        if (event.getClickedInventory().getType() == InventoryType.CHEST) {
                            event.setCancelled(true);
                        }
                    } else if (event.getSlot() < 44 || event.getSlot() > 9) {
                        Class db = com.earth2me.essentials.ItemDb.class;
                        boolean hit = false;
                        for (Method method : db.getDeclaredMethods()) {
                            if (method.getName().equals("serialize")) {
                                hit = true;
                            }
                        }
                        if (!hit) {
                            p.sendMessage("Editing a kit lineup is disabled without EssentialsX, please grab it here. https://www.spigotmc.org/resources/9089/");
                            event.setCancelled(true);
                        }
                    }
                    if (event.getSlot() == 48) {
                        edit.selling();
                    } else if (event.getSlot() == 49) {
                        edit.keys();
                    } else if (event.getSlot() == 50) {
                        edit.gui();
                    } else if (event.getSlot() == 51) {
                        edit.createCommand();
                    } else if (event.getSlot() == 52) {
                        Class db = com.earth2me.essentials.ItemDb.class;
                        for (Method method : db.getDeclaredMethods()) {
                            if (method.getName().equals("serialize")) {
                                edit.saveKit(p, event.getClickedInventory());
                                event.setCancelled(true);
                                return;
                            }
                        }
                        p.sendMessage("Editing a kit lineup is disabled without EssentialsX, please grab it here. https://www.spigotmc.org/resources/9089/");
                        event.setCancelled(true);
                    }
                } else if (plugin.inEditor.get(event.getWhoClicked()) == "decor") {
                    BlockEditor edit = new BlockEditor(plugin.editing.get(p), p);
                    event.setCancelled(true);
                    if (event.getSlot() == 10) {
                        edit.toggleHologram();
                    } else if (event.getSlot() == 12) {
                        edit.toggleParticles();
                    } else if (event.getSlot() == 14) {
                        edit.toggleDisplayItems();
                    } else if (event.getSlot() == 16) {
                        edit.setDisplayItem();
                    }
                } else if (plugin.inEditor.get(event.getWhoClicked()) == "keys") {
                    Editor edit = new Editor(plugin.editingKit.get(p).name, p);
                    event.setCancelled(true);
                    if (event.getSlot() == 11) {
                        if (event.getClick().isLeftClick()) {
                            p.getInventory().addItem(Keys.spawnKey(plugin.editingKit.get(p).name, 1, 1));
                        } else {
                            p.getInventory().addItem(Keys.spawnKey(plugin.editingKit.get(p).name, 64, 1));
                        }
                    } else if (event.getSlot() == 13) {
                        if (event.getClick().isLeftClick()) {
                            p.getInventory().addItem(Keys.spawnKey(plugin.editingKit.get(p).name, 1, 2));
                        } else {
                            p.getInventory().addItem(Keys.spawnKey(plugin.editingKit.get(p).name, 64, 2));
                        }
                    } else if (event.getSlot() == 15) {
                        int amt = plugin.editingKit.get(p).keyReward();
                        if (event.getClick().isLeftClick()) {
                            plugin.editingKit.get(p).setKeyReward(amt + 1);
                        } else {
                            plugin.editingKit.get(p).setKeyReward(amt - 1);
                        }
                        edit.keys();
                    }
                } else if (plugin.inEditor.get(event.getWhoClicked()) == "selling") {
                    Editor edit = new Editor(plugin.editingKit.get(p).name, p);
                    event.setCancelled(true);
                    if (event.getSlot() == 10) {
                        edit.setNoSale();
                    } else if (event.getSlot() == 12) {
                        edit.editLink();
                    } else if (event.getSlot() == 14) {
                        edit.editPrice();
                    } else if (event.getSlot() == 16) {
                        edit.toggleDelay();
                    }
                } else if (plugin.inEditor.get(event.getWhoClicked()) == "gui") {
                    Editor edit = new Editor(plugin.editingKit.get(p).name, p);
                    event.setCancelled(true);
                    if (event.getSlot() == 10) {
                        edit.setTitle();
                    } else if (event.getSlot() == 12) {
                        edit.RemoveTitle();
                    } else if (event.getSlot() == 14) {
                        edit.setKitsDisplayItem();
                    } else if (event.getSlot() == 16) {
                        edit.blacklist();
                    }
                }
                if (event.getCurrentItem().getItemMeta() != null) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.EXIT.getConfigValue()))) {
                            p.closeInventory();
                        }
                        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(Lang.BACK.getConfigValue()))) {
                            if (event.getClickedInventory().getTitle().contains("Editing decor for") || event.getClickedInventory().getTitle().contains("You are editing kit")) {
                                BlockEditor edit = new BlockEditor(plugin.editing.get(p), p);
                                edit.open();
                            } else {
                                Editor edit = new Editor(plugin.editingKit.get(p).name, p);
                                edit.open(false);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        try {
            if (event.getInventory().getTitle().startsWith("Previewing") || plugin.xyz.contains(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
                if (plugin.references.isPlaySound()) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), plugin.references.getSound(), 10.0F, 1.0F);
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        try {
            if (event.getInventory().getTitle().startsWith("Previewing") || plugin.xyz.contains(event.getWhoClicked().getUniqueId())) {
                event.setCancelled(true);
                if (plugin.references.isPlaySound()) {
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), plugin.references.getSound(), 10.0F, 1.0F);
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        try {
            final Player p = (Player) event.getPlayer();
            plugin.buy.remove(p);

            if (plugin.xyz.contains(p.getUniqueId()) || plugin.xyz3.contains(p.getUniqueId()) || plugin.xyz2.contains(p.getUniqueId())) {

                if (plugin.xyz.contains(p.getUniqueId()))
                            plugin.xyz.remove(p.getUniqueId());
                else if (plugin.xyz3.contains(p.getUniqueId()))
                            plugin.xyz3.remove(p.getUniqueId());
                else if (plugin.xyz2.contains(p.getUniqueId()))
                            plugin.xyz2.remove(p.getUniqueId());

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (!p.getOpenInventory().getTopInventory().getType().equals(InventoryType.CHEST))
                        p.closeInventory();
                }, 1L);
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}

