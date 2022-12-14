package me.d2siado.dev.Other.commands;

import me.d2siado.dev.LootBox;
import me.d2siado.dev.Utils.CC;
import me.d2siado.dev.Utils.Plugin;
import me.d2siado.dev.Utils.Stacks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class lootbox implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] strings) {
        final String command = cmd.getName();
        if (!(strings.length >= 1)) {
            Arrays.asList(
                    ("")
                    , ("&6&l" + LootBox.getInstance().getName())
                    , ("  &e[*] &f/" + command + " give")
                    , ("  &e[*] &f/" + command + " loot")
                    , ("  &e[*] &f/" + command + " about")
                    , ("  &e[*] &f/" + command + " reload")
                    , ("")).forEach(m -> sender.sendMessage(CC.translate(m)));
            return true;
        } else if ("give".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission("lootbox.give") || sender.hasPermission("lootbox.*"))) {
                sender.sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.NO-PERMS")));
                return true;
            }
            if (strings.length != 3) {
                sender.sendMessage(CC.translate("&cUse: /" + command + " give {player} {amount}"));
                return true;
            }
            final String playerarg = strings[1];
            if (!playerarg.equalsIgnoreCase("all")) {
                Player target = Bukkit.getPlayer(playerarg);
                if (target == null) {
                    sender.sendMessage(CC.translate("&c" + playerarg + " player doesnt exists..."));
                    return true;
                } else if (!target.isOnline()) {
                    sender.sendMessage(CC.translate("&c" + target.getName() + " inst online..."));
                    return true;
                }
                if (!Plugin.isInt(strings[2])) {
                    sender.sendMessage(CC.translate("&cEnter a valid item amount..."));
                    return true;
                }
                final int amount = Integer.parseInt(strings[2]);
                if (Plugin.isFull(target)) {
                    target.getWorld().dropItem(target.getLocation(), Stacks.getLootboxItem(amount));
                    return true;
                }
                target.getInventory().addItem(Stacks.getLootboxItem(amount));
                return true;
            }
            if (!Plugin.isInt(strings[2])) {
                sender.sendMessage(CC.translate("&cEnter a valid item amount..."));
                return true;
            }
            final int amount = Integer.parseInt(strings[2]);
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (Plugin.isFull(target)) {
                    target.getWorld().dropItem(target.getLocation(), Stacks.getLootboxItem(amount));
                    return true;
                }
                target.getInventory().addItem(Stacks.getLootboxItem(amount));
            }
        } else if ("loot".equalsIgnoreCase(strings[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.NO-CONSOLE")));
                return true;
            }
            ((Player) sender).openInventory(Stacks.getLootInventory((sender.hasPermission("lootbox.loot") || sender.hasPermission("lootbox.*"))));
        } else if ("about".equalsIgnoreCase(strings[0])) {
            Arrays.asList(("")
                    , ("&6&l" + LootBox.getInstance().getName())
                    , ("  &eVersion:&f " + LootBox.getInstance().getDescription().getVersion())
                    , ("  &eLast Update:&f " + LootBox.getInstance().getLastUpdate())
                    , ("  &eAuthor:&f " + LootBox.getInstance().getDescription().getAuthors().toString())
                    , ("")).forEach(m -> sender.sendMessage(CC.translate(m)));
        } else if ("reload".equalsIgnoreCase(strings[0])) {
            if (!(sender.hasPermission("lootbox.reload") || sender.hasPermission("lootbox.*"))) {
                sender.sendMessage(CC.translate(LootBox.getInstance().getConfig().getString("MESSAGES.NO-PERMS")));
                return true;
            }
            try {
                reload();
            } catch (Error e) {
                sender.sendMessage(CC.translate("&cLootBox Plugin was found a error in the config files"));
                return true;
            }
            sender.sendMessage(CC.translate("&aLootBox Plugin was reloaded sussesfuly"));
        } else {
            sender.sendMessage(CC.translate("&cThe subcommand " + strings[0] + " was not founded, please use /" + command));
        }
        return false;
    }

    private void reload() {
        LootBox.getInstance().getConfig().reload();
        LootBox.getInstance().getData().reload();
    }
}
