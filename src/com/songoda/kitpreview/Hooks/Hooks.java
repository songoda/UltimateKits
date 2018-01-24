package com.songoda.kitpreview.Hooks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Created by songoda on 3/16/2017.
 */
public interface Hooks {

    List<String> getKits();

    long getNextUse(String kitName, Player player);

    List<ItemStack> getItems(Player player, String kit, boolean commands);

    boolean canGiveKit(Player player);

    int kitSize(String kitName);

    void createKit(Player player);

    void saveKit(Player player, String kitName, ItemStack[] items);

    boolean hasPermission(Player player, String kitName);

    void giveKit(Player player, String kitName);

    void givePartKit(Player player, String kitName, int amt);

    void updateDelay(Player player, String kitName);

    boolean isReady(Player player, String kitName);

    boolean doesKitExist(String kit);
}
