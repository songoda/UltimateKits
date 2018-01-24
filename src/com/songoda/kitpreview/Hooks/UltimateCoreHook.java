package com.songoda.kitpreview.Hooks;

import bammerbom.ultimatecore.bukkit.UltimateFileLoader;
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.api.UKit;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.configuration.ConfigSection;
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by songoda on 3/16/2017.
 */
public class UltimateCoreHook implements Hooks {

    KitPreview plugin = KitPreview.pl();

    @Override
    public List<String> getKits() {
        List<String> list = new ArrayList<>();
        try {
            List<UKit> kits = UC.getServer().getKits();
            for (UKit kit : kits) {
                list.add(kit.getName());
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return list;
    }

    @Override
    public long getNextUse(String kitName, Player player) {
        try {
            UKit kit = new UKit(kitName);

            return kit.getCooldownFor(player);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return 0;
    }

    @Override
    public boolean canGiveKit(Player player) {
        try {
            if (player.isOp())
                return true;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }

    @Override
    public List<ItemStack> getItems(Player player, String kitName, boolean commands) {
        List<ItemStack> items = new ArrayList<>();
        try {
            UKit kit = new UKit(kitName);
            items = kit.getItems();
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return items;

    }

    @Override
    public int kitSize(String kitName) {
        List<ItemStack> items = new ArrayList<>();
        try {
            UKit kit = new UKit(kitName);
            items = kit.getItems();
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return items.size();
    }

    @Override
    public void createKit(Player player) {

    }

    @Override
    public void saveKit(Player player, String kitName, ItemStack[] items) {
        player.sendMessage(plugin.references.getPrefix() + "UltimateCores save kit method is currently broken. Go yell at him to fix it.");
    }

    @Override
    public boolean hasPermission(Player player, String kitName) {
        try {
            if (player.hasPermission("uc.kit." + kitName.toLowerCase()))
                return true;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }

    @Override
    public void giveKit(Player player, String kitName) {
        try {
            UKit kit = new UKit(kitName);
            List<ItemStack> items = kit.getItems();
            Map<Integer, ItemStack> leftOver = player.getInventory().addItem((ItemStack[]) items.toArray(new ItemStack[items.size()]));
            for (ItemStack is : leftOver.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), is);
            }

        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    @Override
    public void givePartKit(Player player, String kitName, int amt) {
        try {
            UKit kit = new UKit(kitName);
            List<ItemStack> items = kit.getItems();

            amt = items.size() - amt;

            while (amt != 0) {
                int num = ThreadLocalRandom.current().nextInt(0, items.size());
                items.remove(num);
                amt--;
            }

            Map<Integer, ItemStack> leftOver = player.getInventory().addItem((ItemStack[]) items.toArray(new ItemStack[items.size()]));
            for (ItemStack is : leftOver.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), is);
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    @Override
    public void updateDelay(Player player, String kitName) {
        try {
            UKit kit = new UKit(kitName);
            kit.setLastUsed(player, System.currentTimeMillis());
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    @Override
    public boolean isReady(Player player, String kitName) {
        UKit kit = null;
        try {
            kit = new UKit(kitName);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return kit.hasCooldownPassedFor(player);
    }

    @Override
    public boolean doesKitExist(String kit) {
        try {
            Config config = new Config(UltimateFileLoader.Dkits);
            ConfigSection kitNode = config.getConfigurationSection(kit.toLowerCase());
            if (kitNode != null)
                return true;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }
}
