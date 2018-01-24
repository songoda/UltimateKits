package com.songoda.kitpreview.Utils;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songo on 6/4/2017.
 */
public class SettingsManager implements Listener {

    KitPreview plugin = KitPreview.pl();

    private static ConfigWrapper defs;

    public SettingsManager() {
        plugin.saveResource("SettingDefinitions.yml", true);
        defs = new ConfigWrapper(plugin, "", "SettingDefinitions.yml");
        defs.createNewFile("Loading data file", "KitPreview SettingDefinitions file");
        plugin.getServer().getPluginManager().registerEvents( this, plugin);
    }

    public Map<Player, String> current = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getInventory().getTitle().equals("KitPreview Settings Editor")) {
                e.setCancelled(true);

                String key = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                Player p = (Player) e.getWhoClicked();

                if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Boolean")) {
                    boolean bool = (Boolean) plugin.getConfig().get(key);
                    if (!bool)
                        plugin.getConfig().set(key, true);
                    else
                        plugin.getConfig().set(key, false);
                    finishEditing(p);
                } else {
                    editObject(p, key);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (current.containsKey(p)) {
            if (plugin.getConfig().get("settings." + current.get(p)).getClass().getName().equals("java.lang.Integer")) {
                plugin.getConfig().set("settings." + current.get(p), Integer.parseInt(e.getMessage()));
            } else if (plugin.getConfig().get("settings." + current.get(p)).getClass().getName().equals("java.lang.Double")) {
                plugin.getConfig().set("settings." + current.get(p), Double.parseDouble(e.getMessage()));
            } else if (plugin.getConfig().get("settings." + current.get(p)).getClass().getName().equals("java.lang.String")) {
                plugin.getConfig().set("settings." + current.get(p), e.getMessage());
            }
            finishEditing(p);
            e.setCancelled(true);
        }
    }

    public void finishEditing(Player p) {
        current.remove(p);
        plugin.saveConfig();
        openEditor(p);
    }


    public void editObject(Player p, String current) {
        this.current.put(p, current);
        p.closeInventory();
        p.sendMessage("");
        p.sendMessage(Arconix.pl().format().formatText("&7Please enter a value for &6"+current+"&7."));
        if (plugin.getConfig().get(current).getClass().getName().equals("java.lang.Integer")) {
            p.sendMessage(Arconix.pl().format().formatText("&cUse only numbers."));
        }
        p.sendMessage("");
    }

    public static void openEditor(Player p) {
        KitPreview plugin = KitPreview.pl();
        Inventory i = Bukkit.createInventory(null, 54, "KitPreview Settings Editor");

        int num = 0;
        ConfigurationSection cs = plugin.getConfig().getConfigurationSection("");
        for (String key : cs.getKeys(true)) {

            if (!key.contains("data")) {
                ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Arconix.pl().format().formatText("&6" + key));
                ArrayList<String> lore = new ArrayList<>();
                if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Boolean")) {

                    boolean bool = (Boolean) plugin.getConfig().get(key);

                    if (!bool)
                        lore.add(Arconix.pl().format().formatText("&c" + bool));
                    else
                        lore.add(Arconix.pl().format().formatText("&a" + bool));

                } else if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.String")) {
                    String str = (String) plugin.getConfig().get(key);
                    lore.add(Arconix.pl().format().formatText("&9" + str));
                } else if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Integer")) {

                    int in = (Integer) plugin.getConfig().get(key);
                    lore.add(Arconix.pl().format().formatText("&5" + in));
                }
                if (defs.getConfig().contains(key)) {
                    String text = defs.getConfig().getString(key);
                    int index = 0;
                    while (index < text.length()) {
                        lore.add(Arconix.pl().format().formatText("&7" + text.substring(index, Math.min(index + 50, text.length()))));
                        index += 50;
                    }
                }
                meta.setLore(lore);
                item.setItemMeta(meta);

                i.setItem(num, item);
                num++;
            }
        }
        p.openInventory(i);
    }
}
