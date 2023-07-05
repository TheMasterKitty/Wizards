package wizards.wizards.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wizards.wizards.Wizards;
import wizards.wizards.misc.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GiveWandCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(Utils.colored("&cCommand usage: /givewand <wand-type> or /givewand <wand-type> <player>"));
            return false;
        }
        else if (strings.length == 1) {
            if (commandSender instanceof Player p) {
                if (Wizards.wandItems.containsKey(strings[0])) {
                    p.getInventory().addItem(Wizards.wandItems.get(strings[0]).clone());
                    p.sendMessage(Utils.colored("&aGave yourself a " + strings[0] + " wand."));
                    return true;
                }
                else {
                    p.sendMessage(Utils.colored("&cThat's not a valid wand!"));
                    return false;
                }
            }
            else {
                commandSender.sendMessage(Utils.colored("&cYou have to be a player to give yourself a wand!"));
                return false;
            }
        }
        else {
            if (Wizards.wandItems.containsKey(strings[0])) {
                AtomicBoolean first = new AtomicBoolean(true);
                AtomicReference<String> usernames = new AtomicReference<>("");
                List.of(strings).forEach(string -> {
                    if (!first.get()) {
                        try {
                            Objects.requireNonNull(Bukkit.getServer().getPlayer(string)).getInventory().addItem(Wizards.wandItems.get(strings[0]).clone());
                            usernames.set(usernames.get() + string + ", ");
                        }
                        catch (Exception ignored) { }
                    }
                    first.set(false);
                });
                if (usernames.get().length() == 0) {
                    commandSender.sendMessage(Utils.colored("&cNo players by names listed found."));
                    return false;
                }
                else {
                    commandSender.sendMessage(Utils.colored("&aGave a " + strings[0] + " wand to " + usernames.get().substring(0, usernames.get().length() - 3) + "."));
                    return true;
                }
            }
            else {
                commandSender.sendMessage(Utils.colored("&cThat's not a valid wand!"));
                return false;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            return new ArrayList<>(Wizards.wands.keySet());
        }

        return null;
    }
}
