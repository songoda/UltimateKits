package com.songoda.kitpreview;

import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Sound;

public class References {

    private String prefix = null;
    private boolean playSound = false;
    private Sound sound = null;

    public References() {
        try {
            prefix = Lang.PREFIX.getConfigValue() + " ";
            playSound = KitPreview.pl().getConfig().getBoolean("EnableSound");
            sound = Sound.valueOf(KitPreview.pl().getConfig().getString("Sound"));
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean isPlaySound() {
        return this.playSound;
    }

    public Sound getSound() {
        return this.sound;
    }
}
