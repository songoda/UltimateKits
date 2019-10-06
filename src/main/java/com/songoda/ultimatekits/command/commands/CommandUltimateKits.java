package com.songoda.ultimatekits.command.commands;

import com.songoda.ultimatekits.UltimateKits;
import com.songoda.ultimatekits.command.AbstractCommand;
import com.songoda.ultimatekits.utils.Methods;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandUltimateKits extends AbstractCommand {

    public CommandUltimateKits() {
        super(null, false, "KitAdmin");
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(UltimateKits instance, CommandSender sender, String... args) {
        sender.sendMessage("");
        instance.getLocale().newMessage("&7Version " + instance.getDescription().getVersion()
                + " Created with <3 by &5&l&oSongoda").sendPrefixedMessage(sender);

        for (AbstractCommand command : instance.getCommandManager().getCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(Methods.formatText("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(UltimateKits instance, CommandSender sender, String... args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/KitAdmin";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}