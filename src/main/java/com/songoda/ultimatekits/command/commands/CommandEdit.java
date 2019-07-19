package com.songoda.ultimatekits.command.commands;

import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.command.AbstractCommand;
import com.songoda.ultimatekits.gui.GUIBlockEditor;
import com.songoda.ultimatekits.gui.GUIKitEditor;
import com.songoda.ultimatekits.kit.KitBlockData;
import com.songoda.ultimatekits.utils.Methods;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEdit extends AbstractCommand {

    public CommandEdit(AbstractCommand parent) {
        super("edit", parent, true, false);
    }

    @Override
    protected ReturnType runCommand(UltimateKits instance, CommandSender sender, String... args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 200);
        KitBlockData kitBlockData = instance.getKitManager().getKit(block.getLocation());
        if (args.length > 2) return ReturnType.SYNTAX_ERROR;

        if (args.length == 1) {
            if (kitBlockData == null) {
                instance.getLocale().newMessage("&8This block does not contain a kit.").sendPrefixedMessage(player);
                return ReturnType.FAILURE;
            }
            new GUIBlockEditor(instance, player, block.getLocation());
        } else {
            String kitStr = args[1].toLowerCase().trim();
            if (instance.getKitManager().getKit(kitStr) == null) {
                instance.getLocale().getMessage("command.kit.kitdoesntexist").sendPrefixedMessage(player);
                return ReturnType.FAILURE;
            }

            new GUIKitEditor(instance, player, instance.getKitManager().getKit(kitStr), null, null, 0);
        }
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "ultimatekits.admin";
    }

    @Override
    public String getSyntax() {
        return "/KitAdmin edit <kit>";
    }

    @Override
    public String getDescription() {
        return "Edit a kit.";
    }
}
