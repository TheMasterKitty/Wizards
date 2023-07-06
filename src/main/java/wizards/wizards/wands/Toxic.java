package wizards.wizards.wands;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wizards.wizards.Wizards;
import wizards.wizards.misc.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Toxic extends Wand {
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        Entity tar = user.getTargetEntity(10);
        if (tar instanceof LivingEntity target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 2));
            target.sendMessage(Utils.colored("&2You've been poisoned by " + user.getName()));
            user.sendMessage(Utils.colored("&2Poisened " + target.getName()));
            mainCooldowns.put(user.getUniqueId(), 8);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (mainCooldowns.get(user.getUniqueId()) > 0)
                        mainCooldowns.put(user.getUniqueId(), mainCooldowns.get(user.getUniqueId()) - 1);
                    else cancel();
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
        }
    }

    @Override
    public void onUseWandSecondary(Player user) {
        Location location = user.getLocation();
        secondCooldowns.put(user.getUniqueId(), 80);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (secondCooldowns.get(user.getUniqueId()) <= 60) cancel();
                location.getNearbyLivingEntities(10).forEach(entity -> {
                    if (entity.getUniqueId() != user.getUniqueId())
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 15, 0));
                });
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (secondCooldowns.get(user.getUniqueId()) > 0)
                    secondCooldowns.put(user.getUniqueId(), secondCooldowns.get(user.getUniqueId()) - 1);
                else cancel();
                if (secondCooldowns.get(user.getUniqueId()) > 60) {
                    for (int i = 0; i < 100; i++) {
                        double angle = i * ((2 * Math.PI) / 100);
                        for (double j = 0; j < 8; j += 0.25) {
                            double x = location.getX() + j * Math.cos(angle);
                            double z = location.getZ() + j * Math.sin(angle);
                            Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);
                            location.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(Color.GREEN, 2));
                        }
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
    }

    @Override
    public String mainAbility() {
        return "Toxic Spray";
    }

    @Override
    public String secondaryAbility() {
        return "Poison Gas";
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
