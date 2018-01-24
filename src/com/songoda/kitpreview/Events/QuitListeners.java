package com.songoda.kitpreview.Events;

import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListeners implements Listener {

    KitPreview plugin = KitPreview.pl();

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        try {
            plugin.inEditor.remove(event.getPlayer());
            plugin.xyz.remove(event.getPlayer().getUniqueId());
            plugin.xyz2.remove(event.getPlayer().getUniqueId());
            plugin.xyz3.remove(event.getPlayer().getUniqueId());

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}

