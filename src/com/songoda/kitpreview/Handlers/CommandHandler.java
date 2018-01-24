package com.songoda.kitpreview.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.*;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import com.songoda.kitpreview.Utils.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by songoda on 2/24/2017.
 */
public class CommandHandler implements CommandExecutor {

    private final KitPreview plugin;

    public CommandHandler(final KitPreview plugin) {
        this.plugin = plugin;
    }

    public void help(CommandSender sender, int page) {
        sender.sendMessage("");
        sender.sendMessage(Arconix.pl().format().formatText("&7Page: &a" + page + " of 2 ======================"));
        if (page == 1) {
            sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&7" + plugin.getDescription().getVersion() + " Created by &5&l&oBrianna"));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&cKits &7View all available kits."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&dPK <kit> &7Preview a kit."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP help <page> &7Displays this page."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP reload &7Reload the Configuration and Language files."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP edit <kit> &7Edit a kit."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP set <kit> &7Make the block you are looking at display a kit"));
        } else if (page == 2) {
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP remove &7Remove a kit from the block you are looking at."));
            sender.sendMessage(Arconix.pl().format().formatText(" &8- " + "&aKP key <kit> <Ultra/Regular> <player/all> <amount> &7Give a kit-specific key to the players of your server. These keys can be used to purchase kits."));
            sender.sendMessage(Arconix.pl().format().formatText("&aTo edit a kit block hold shift and right click it."));
        } else {
            sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "That page does not exist!"));
        }
        sender.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (cmd.getName().equalsIgnoreCase("kits")) {
                KitsGUI.show((Player) sender, 1);
            }
            if (cmd.getName().equalsIgnoreCase("kit")) {
                if (args.length == 0) {
                    KitsGUI.show((Player) sender, 1);
                } else if (args.length == 1) {
                    Player p = (Player) sender;
                    String kitName = args[0].toLowerCase();
                    if (!plugin.hooks.doesKitExist(kitName)) {
                        p.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kitName));
                    } else {
                        Kit kit = new Kit(null, kitName);
                        if (sender.hasPermission("kitpreview.admin")) {
                            kit.give(p, false, false, true);
                        } else {
                            kit.buy(p);
                        }
                    }
                } else if (args.length == 2) {
                    String kitName = args[0].toLowerCase();
                    if (!plugin.hooks.doesKitExist(kitName)) {
                        sender.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kitName));
                    } else {
                        if (Bukkit.getPlayerExact(args[1]) == null) {
                            sender.sendMessage(plugin.references.getPrefix() + Lang.PLAYER_NOT_FOUND.getConfigValue(kitName));
                        } else {
                            Player p2 = Bukkit.getPlayer(args[1]);
                            if (!(sender instanceof Player)) {
                                Kit kit = new Kit(null, kitName);
                                kit.give(p2, false, false, true);
                                sender.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&7You gave &9" + p2.getDisplayName() + "&7 kit &9" + kit.name + "&7."));
                            } else {
                                Player p = (Player) sender;
                                if (!plugin.hooks.canGiveKit(p)) {
                                    p.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                                } else {
                                    Kit kit = new Kit(null, kitName);
                                    kit.give(p2, false, false, true);
                                    p.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&7You gave &9" + p2.getDisplayName() + "&7 kit &9" + kit.name + "&7."));
                                }
                            }
                        }
                    }
                } else {
                    sender.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText(Lang.SYNTAX.getConfigValue()));
                }
            }
            if (cmd.getName().equalsIgnoreCase("kitpreview")) {
                if (plugin.getConfig().getBoolean("Lock-KP-Commands") && !sender.hasPermission("kitpreview.admin")) {
                    sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&7" + plugin.getDescription().getVersion() + " " + Lang.NO_PERM.getConfigValue()));
                } else {
                    if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                        if (args.length == 2) {
                            help(sender, Integer.parseInt(args[1]));
                        } else {
                            help(sender, 1);
                        }
                    } else if (args[0].equalsIgnoreCase("edit")) {
                        Player p = (Player) sender;
                        Block b = p.getTargetBlock((Set<Material>) null, 200);
                        String loc = Arconix.pl().serialize().serializeLocation(b);
                        if (args.length <= 2) {
                            if (!p.hasPermission("kitpreview.admin")) {
                                p.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                            } else {
                                if (args.length == 1) {
                                    if (plugin.getConfig().getString("data.block." + loc) == null) {
                                        p.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8This block does not contain a kit."));
                                    } else {
                                        BlockEditor edit = new BlockEditor(b, p);
                                        edit.open();
                                    }
                                } else if (args.length == 2) {
                                    String kit = args[1].toLowerCase();
                                    if (!plugin.hooks.doesKitExist(kit)) {
                                        p.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                                    } else {
                                        Editor edit = new Editor(kit, p);
                                        edit.open(false);
                                    }
                                }
                            }
                        }
 /*               } else if (args[0].equalsIgnoreCase("atest")) { // creates a kit
                    //plugin.hoooks.createKit((Player)sender);
                } else if (args[0].equalsIgnoreCase("loltest")) {
                    Location location = ((Player)sender).getLocation();

                    ItemStack is = new ItemStack(Material.CHEST);

                    ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
                    as.setHelmet(is);
                    as.setGravity(false);
                    as.setCanPickupItems(false);
                    as.setVisible(false);
                    as.setCollidable(false);

                    double oy = as.getLocation().getY();

                    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        public void run() {
                            Location loc = as.getLocation();
                            float yaw = loc.getYaw();

                            double y = loc.getY();
                            String cur = null;
                            if (as.hasMetadata("job")) {
                                cur = as.getMetadata("job").get(0).asString();
                            }
                            if (y < (oy + .07) && cur == null) {
                                y = y + .005;
                            } else if (y > (oy - .07)) {
                                as.setMetadata("job", new FixedMetadataValue(plugin, "Down"));
                                y = y - .005;
                            } else {
                                as.removeMetadata("job", plugin);
                            }
                            loc.setYaw(yaw + 5);
                            loc.setY(y);
                            as.teleport(loc);
                        }
                    }, 1L, 1L); */
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (!sender.hasPermission("kitpreview.admin")) {
                            sender.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        } else {
                            plugin.reload();
                            sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Configuration and Language files reloaded."));
                        }
                    } else if (args[0].equalsIgnoreCase("settings")) {
                        if (!sender.hasPermission("kitpreview.admin")) {
                            sender.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                        } else {
                            Player p = (Player) sender;
                            SettingsManager.openEditor(p);
                        }
                    } else if (args[0].equalsIgnoreCase("key")) {
                        if (args.length != 4 && args.length != 5) {
                            sender.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText(Lang.SYNTAX.getConfigValue()));
                        } else {
                            if (!sender.hasPermission("kitpreview.admin")) {
                                sender.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                            } else {
                                String kit = args[1].toLowerCase();
                                if (!plugin.hooks.doesKitExist(kit)) {
                                    sender.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                                } else {
                                    if (Bukkit.getPlayer(args[3]) == null && !args[3].trim().equalsIgnoreCase("all")) {
                                        sender.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&cThat username does not exist, or the user is offline!"));
                                    } else {
                                        int amt = 1;
                                        if (args.length == 5) {
                                            if (!Arconix.pl().doMath().isNumeric(args[4])) {
                                                amt = 0;
                                            } else {
                                                amt = Integer.parseInt(args[4]);
                                            }
                                        }
                                        if (amt == 0) {
                                            sender.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&a" + args[3] + " &cis not a number."));
                                        } else {
                                            int type = 1;
                                            if (args[2].equalsIgnoreCase("Ultra")) {
                                                type = 2;
                                            }
                                            if (!args[3].trim().equals("all")) {
                                                Player p = Bukkit.getPlayer(args[3]);
                                                p.getInventory().addItem(Keys.spawnKey(kit, amt, type));
                                                p.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&9You have received a &a" + kit + " &9kit key."));
                                            } else {
                                                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                                                    pl.getInventory().addItem(Keys.spawnKey(kit, amt, type));
                                                    pl.sendMessage(plugin.references.getPrefix() + Arconix.pl().format().formatText("&9You have received a &a" + kit + " &9kit key."));

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("set")) {
                        if (args.length != 2) {
                            sender.sendMessage(plugin.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                        } else {
                            if (!sender.hasPermission("kitpreview.admin")) {
                                sender.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                            } else {
                                Player player = (Player) sender;
                                String kit = args[1].toLowerCase();
                                if (!plugin.hooks.doesKitExist(kit)) {
                                    player.sendMessage(plugin.references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(kit));
                                } else {
                                    Block b = player.getTargetBlock((Set<Material>) null, 200);
                                    String loc = Arconix.pl().serialize().serializeLocation(b);
                                    plugin.getConfig().set("data.block." + loc, kit);
                                    plugin.saveConfig();
                                    sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Kit &a" + kit + " &8set to: &a" + b.getType().toString() + "&8."));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length != 1) {
                            sender.sendMessage(plugin.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                        } else {
                            if (!sender.hasPermission("kitpreview.admin")) {
                                sender.sendMessage(plugin.references.getPrefix() + Lang.NO_PERM.getConfigValue());
                            } else {
                                Player player = (Player) sender;
                                Block b = player.getTargetBlock((Set<Material>) null, 200);
                                Kit kit = new Kit(b);
                                kit.removeKitFromBlock(player);
                            }
                        }
                    } else {
                        sender.sendMessage(Arconix.pl().format().formatText(plugin.references.getPrefix() + "&8Invalid argument.. Looking for &9/pk " + args[0] + "&8?"));
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase("previewkit")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.references.getPrefix() + Lang.NO_CONSOLE_ACCESS.getConfigValue());
                } else {
                    Player p = (Player) sender;
                        if (args.length != 1) {
                            p.sendMessage(plugin.references.getPrefix() + Lang.PREVIEW_NO_KIT_SUPPLIED.getConfigValue());
                        } else {
                            Kit kit = new Kit(null, args[0].toLowerCase());
                            kit.display(p, false);
                        }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
        return true;
    }

}
