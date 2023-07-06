package wizards.wizards.wands;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wizards.wizards.Wizards;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cloaked extends Wand {
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        mainCooldowns.put(user.getUniqueId(), 30);
        user.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400, 1, true));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (mainCooldowns.get(user.getUniqueId()) > 0)
                    mainCooldowns.put(user.getUniqueId(), mainCooldowns.get(user.getUniqueId()) - 1);
                else cancel();
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
    }

    @Override
    public void onUseWandSecondary(Player user) {
        secondCooldowns.put(user.getUniqueId(), 33);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (secondCooldowns.get(user.getUniqueId()) == 8) cancel();
                for (int i = 0; i < 100; i++) {
                    double angle = i * ((2 * Math.PI) / 100);
                    double x = user.getLocation().getX() + Math.cos(angle);
                    double z = user.getLocation().getZ() + Math.sin(angle);

                    Location particleLocation = new Location(user.getWorld(), x, user.getLocation().getY() + 1, z);
                    user.getWorld().spawnParticle(Particle.END_ROD, particleLocation, 1);
                }
                user.getLocation().getNearbyLivingEntities(3).forEach(entity -> {
                    if (entity.getUniqueId() != user.getUniqueId()) {
                        double angle = Math.atan2(entity.getLocation().getZ() - user.getLocation().getZ(), entity.getLocation().getX() - user.getLocation().getX());
                        double x = user.getLocation().getX() - 0.2 * Math.cos(angle);
                        double z = user.getLocation().getZ() + 0.2 * Math.sin(angle);
                        entity.setVelocity(new Vector(entity.getVelocity().getX() + x, entity.getVelocity().getY(), entity.getVelocity().getZ() + z));
                    }
                });
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (secondCooldowns.get(user.getUniqueId()) > 0)
                    secondCooldowns.put(user.getUniqueId(), secondCooldowns.get(user.getUniqueId()) - 1);
                else cancel();
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
    }

    @Override
    public String mainAbility() {
        return "Invisibility Cloak";
    }

    @Override
    public String secondaryAbility() {
        return "Protection Cloak";
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
