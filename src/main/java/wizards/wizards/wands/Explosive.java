package wizards.wizards.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wizards.wizards.Wizards;
import wizards.wizards.misc.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Explosive extends Wand {
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        Block b = Utils.getTargetBlock(user, 20);
        if (b != null) {
            for (int i = 0; i < 3; i++) {
                b.getWorld().spawnEntity(b.getLocation().add(0, 2, 0), EntityType.PRIMED_TNT);
            }
            mainCooldowns.put(user.getUniqueId(), 15);
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
            Entity target = user.getTargetEntity(20);
            if (target != null) {
                for (int i = 0; i < 3; i++) {
                    target.getWorld().spawnEntity(target.getLocation().add(0, 2, 0), EntityType.PRIMED_TNT);
                }
                mainCooldowns.put(user.getUniqueId(), 15);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (mainCooldowns.get(user.getUniqueId()) > 0)
                            mainCooldowns.put(user.getUniqueId(), mainCooldowns.get(user.getUniqueId()) - 1);
                        else cancel();
                    }
                }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
            }
            else
                user.sendMessage(Utils.colored("&cNo block or player in sight to spawn on."));
        }
    }

    @Override
    public void onUseWandSecondary(Player user) {
        secondCooldowns.put(user.getUniqueId(), 120);
        Location start = user.getLocation();
        Location current = user.getLocation().add(0, -1, 0);
        while (!current.getBlock().isSolid()) current = current.add(0, -1, 0);
        current.getBlock().setType(Material.TNT);
        for (int i = 0; i < 200; i++) {
            Vector addition = new Vector(0, 0, 0);
            boolean first = true;
            int attempts = 0;
            while ((!current.add(addition).getBlock().isSolid() || first) && current.add(addition).getBlock().getType() != Material.TNT && attempts < 50) {
                addition = new Vector(new Random().nextInt(-2,3), new Random().nextInt(-2, 3), new Random().nextInt(-2, 3));
                first = false;
                attempts++;
            }
            if (current.add(addition).getBlock().isSolid()) {
                current = current.add(addition);
                current.getBlock().setType(Material.TNT);
            }
        }
        start.getBlock().setType(Material.CRIMSON_BUTTON);
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
        return "Tactical Missile";
    }

    @Override
    public String secondaryAbility() {
        return "TNT Tunnel";
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
