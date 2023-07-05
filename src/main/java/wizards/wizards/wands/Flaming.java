package wizards.wizards.wands;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import wizards.wizards.Wizards;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Flaming extends Wand {
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        mainCooldowns.put(user.getUniqueId(), 5);
        Projectile fireball = user.launchProjectile(Fireball.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireball.isDead()) cancel();
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(226, 88, 34), 1.5f);
                fireball.getWorld().spawnParticle(Particle.REDSTONE, fireball.getLocation(), 1, dustOptions);
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 2);
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
        secondCooldowns.put(user.getUniqueId(), 30);
        user.getNearbyEntities(10, 2, 10).forEach(entity -> entity.setFireTicks(200));
        Location location = user.getLocation();

        final int[] particleOn = { 0 };
        new BukkitRunnable() {
            @Override
            public void run() {
                if (particleOn[0] == 6) cancel();
                for (int i = 0; i < 100; i++) {
                    double angle = i * ((2 * Math.PI) / 100);
                    double x = location.getX() + 8.0 * Math.cos(angle);
                    double z = location.getZ() + 8.0 * Math.sin(angle);

                    Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);
                    location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLocation, 1);
                }
                particleOn[0]++;
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 5);
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
        return "Fireblast";
    }

    @Override
    public String secondaryAbility() {
        return "Flame Ring";
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
