package wizards.wizards.wands;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import wizards.wizards.Wizards;
import wizards.wizards.misc.Utils;

import java.util.*;

public class Void extends Wand {
    public final List<UUID> usingRTP = new ArrayList<>();
    private final Map<UUID, Integer> mainCooldowns = new HashMap<>();
    private final Map<UUID, Integer> secondCooldowns = new HashMap<>();
    @Override
    public void onUseWandMain(Player user) {
        mainCooldowns.put(user.getUniqueId(), 10);
        user.launchProjectile(EnderPearl.class).setShooter(user);
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
        if (user.isSneaking()) {
            usingRTP.add(user.getUniqueId());
            final int[] tpLeft = {5};
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (tpLeft[0] > 0 && user.isSneaking() && Utils.isVectorZero(user.getVelocity())) {
                        user.sendMessage(Utils.colored("&2Random teleportation commencing in " + tpLeft[0] + " seconds... Keep holding sneak and staying still."));
                        tpLeft[0]--;
                    }
                    else if (!user.isSneaking()) {
                        user.sendMessage(Utils.colored("&cYou stopped sneaking so the teleportation was cancelled."));
                        cancel();
                    }
                    else if (!Utils.isVectorZero(user.getVelocity())) {
                        user.sendMessage(Utils.colored("&cYou moved so the teleportation was cancelled."));
                        cancel();
                    }
                    else {
                        int x = new Random().nextInt(user.getLocation().getBlockX() - 1000, user.getLocation().getBlockX() + 500);
                        int z = new Random().nextInt(user.getLocation().getBlockZ() - 1000, user.getLocation().getBlockZ() + 500);
                        while (!user.getWorld().getHighestBlockAt(x, z).isSolid() || !user.getWorld().getWorldBorder().isInside(user.getWorld().getHighestBlockAt(x, z).getLocation())) {
                            x = new Random().nextInt(user.getLocation().getBlockX() - 1000, user.getLocation().getBlockX() + 500);
                            z = new Random().nextInt(user.getLocation().getBlockZ() - 1000, user.getLocation().getBlockZ() + 500);
                        }
                        user.teleport(new Location(user.getWorld(), x, user.getWorld().getHighestBlockAt(x, z).getY() + 1, z));
                        user.sendMessage(Utils.colored("&2Teleport successfully ran."));
                        secondCooldowns.put(user.getUniqueId(), 6000);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (secondCooldowns.get(user.getUniqueId()) > 0)
                                    secondCooldowns.put(user.getUniqueId(), secondCooldowns.get(user.getUniqueId()) - 1);
                                else cancel();
                            }
                        }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
                        cancel();
                    }
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 20);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (usingRTP.contains(user.getUniqueId())) {
                        user.spawnParticle(Particle.ENCHANTMENT_TABLE, user.getLocation(), 3);
                    }
                    else cancel();
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Wizards.class), 0, 1);
            usingRTP.remove(user.getUniqueId());
        }
        else {
            user.sendMessage(Utils.colored("&cUse sneak while activating the ability to confirm random teleportation"));
        }
    }

    @Override
    public String mainAbility() {
        return "Pearl";
    }

    @Override
    public String secondaryAbility() {
        return "Random Tele-portal";
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
