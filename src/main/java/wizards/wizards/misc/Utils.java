package wizards.wizards.misc;

import org.bukkit.util.Vector;

public class Utils {
    public static String colored(String text) {
        return text.replaceAll("&", "§");
    }
    public static boolean isVectorZero(Vector input) {
        return Math.abs(input.getX()) < 0.1 && Math.abs(input.getY()) < 0.1 && Math.abs(input.getZ()) < 0.1;
    }
}
