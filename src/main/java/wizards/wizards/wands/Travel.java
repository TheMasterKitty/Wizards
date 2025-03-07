package wizards.wizards.wands;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wizards.wizards.Wizards;
import wizards.wizards.misc.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Travel extends Wand {
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        if (user.isGliding()) {
            ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
            FireworkMeta meta = (FireworkMeta) firework.getItemMeta();

            FireworkEffect.Builder effectBuilder = FireworkEffect.builder();
            effectBuilder.withColor(Color.RED);
            effectBuilder.withFade(Color.YELLOW);
            effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
            effectBuilder.flicker(true);
            effectBuilder.trail(true);

            meta.addEffect(effectBuilder.build());
            meta.setPower(10);
            firework.setItemMeta(meta);
            user.fireworkBoost(firework);

            user.sendMessage(Utils.colored("&2Succesfully boosted!"));

            mainCooldowns.put(user.getUniqueId(), 12);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (mainCooldowns.get(user.getUniqueId()) > 0)
                        mainCooldowns.put(user.getUniqueId(), mainCooldowns.get(user.getUniqueId()) - 1);
                    else cancel();
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
        }
        else {
            user.sendMessage(Utils.colored("&cYou have to be using an elytra to make a wind tunnel."));
        }
    }

    @Override
    public void onUseWandSecondary(Player user) {
        if (user.isGliding()) {
            user.setVelocity(user.getVelocity().add(new Vector(0, 4, 0)));
            secondCooldowns.put(user.getUniqueId(), 16);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (secondCooldowns.get(user.getUniqueId()) > 0)
                        secondCooldowns.put(user.getUniqueId(), secondCooldowns.get(user.getUniqueId()) - 1);
                    else cancel();
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
        }
        else {
            user.sendMessage(Utils.colored("&cYou have to be using an elytra to boost up!"));
        }
    }

    @Override
    public String mainAbility() {
        return "Wind Tunnel";
    }

    @Override
    public String secondaryAbility() {
        return "Sky-high";
    }

    @Override
    public int getMainCooldown(Player user) {
        if (!mainCooldowns.containsKey(user.getUniqueId())) return 0;
        return mainCooldowns.get(user.getUniqueId());
    }

    @Override
    public int getSecondaryCooldown(Player user) {
        if (!secondCooldowns.containsKey(user.getUniqueId())) return 0;
        return secondCooldowns.get(user.getUniqueId());
    }
}
