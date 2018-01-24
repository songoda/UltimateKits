package com.songoda.kitpreview.Events;

import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.Kit;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListeners implements Listener {

    private KitPreview plugin = KitPreview.pl();

    @EventHandler
    public void onCreateSign(SignChangeEvent e) {
        try {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[previewkit]")) {
            if (!p.hasPermission("previewkit.sign.create")) {
                e.getBlock().breakNaturally();
                p.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
            } else {
                String kitName = e.getLine(1);
                if (!this.plugin.hooks.doesKitExist(kitName.toLowerCase())) {
                    e.getBlock().breakNaturally();
                    p.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kitName));
                } else {
                    e.setLine(0, Lang.SIGN_TITLE.getConfigValue());
                    e.setLine(1, StringUtils.capitalize(kitName));
                    p.sendMessage(plugin.references.getPrefix() + Lang.PREVIEW_SIGN_CREATED.getConfigValue(kitName));
                }
            }
        }
    } catch (Exception ex) {
        Debugger.runReport(ex);
    }
    }

    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        try {
        if ((e.getClickedBlock() != null)) {
            if ((e.getClickedBlock().getState() instanceof Sign)) {
            Player p = e.getPlayer();
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(0).equals(Lang.SIGN_TITLE.getConfigValue())) {
                if (!p.hasPermission("previewkit.sign.use")) {
                    p.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                } else {
                    String kitName = s.getLine(1);
                    Kit kit = new Kit(null, kitName);
                    kit.display(p, false);
                    p.sendMessage(plugin.references.getPrefix() + Lang.PREVIEWING_KIT.getConfigValue(kitName));
                }
            }
            }
        }
    } catch (Exception ex) {
        Debugger.runReport(ex);
    }
    }
}

