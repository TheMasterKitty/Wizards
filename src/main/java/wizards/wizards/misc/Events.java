package wizards.wizards.misc;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import wizards.wizards.Wizards;
import wizards.wizards.wands.Void;
import wizards.wizards.wands.Wand;

import java.util.Objects;

public class Events implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.STICK) {
            NBTItem nbti = new NBTItem(event.getItem());
            if (nbti.hasKey("wand") && Wizards.wands.containsKey(nbti.getString("wand"))) {
                Wand wand = Wizards.wands.get(nbti.getString("wand"));
                Player p = event.getPlayer();
                if (event.getAction().isLeftClick()) {
                    if (wand.getMainCooldown(p) > 0) {
                        p.sendMessage(Utils.colored("&cYour " + wand.mainAbility() + " ability is on cooldown with " + wand.getMainCooldown(p) + " seconds remaining."));
                    }
                    else {
                        wand.onUseWandMain(p);
                        p.sendMessage(Utils.colored("&6Activated your " + wand.mainAbility() + " ability!"));
                    }
                }
                else if (event.getAction().isRightClick()) {
                    if (wand.getSecondaryCooldown(p) > 0) {
                        p.sendMessage(Utils.colored("&cYour " + wand.secondaryAbility() + " ability is on cooldown with " + wand.getSecondaryCooldown(p) + " seconds remaining."));
                    }
                    else {
                        if (wand instanceof Void v) {
                            if (!v.usingRTP.contains(p.getUniqueId())) {
                                wand.onUseWandSecondary(p);
                                p.sendMessage(Utils.colored("&6Activated your " + wand.secondaryAbility() + " ability!"));
                            }
                        }
                        else {
                            wand.onUseWandSecondary(p);
                            p.sendMessage(Utils.colored("&6Activated your " + wand.secondaryAbility() + " ability!"));
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwningPlayer(e.getPlayer());
        item.setItemMeta(skullMeta);
        e.getDrops().add(item);
    }
    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        if (Objects.requireNonNull(((SkullMeta) Objects.requireNonNull(e.getInventory().getItem(5)).getItemMeta()).getOwningPlayer()).getUniqueId() == e.getWhoClicked().getUniqueId()) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(Utils.colored("&cYou can't use your own head silly!"));
        }
    }
}
