package wizards.wizards.misc;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Utils {
    public static String colored(String text) {
        return text.replaceAll("&", "§");
    }
    public static boolean isVectorZero(Vector input) {
        return Math.abs(input.getX()) < 0.1 && Math.abs(input.getY()) < 0.1 && Math.abs(input.getZ()) < 0.1;
    }
    public static Block getTargetBlock(Player player, int range) {
        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), range);
        if (result != null && result.getHitBlock() != null) {
            return result.getHitBlock();
        }
        return null;
    }

}
