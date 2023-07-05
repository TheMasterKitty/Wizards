package wizards.wizards.wands;

import org.bukkit.entity.Player;

public abstract class Wand {
    public abstract void onUseWandMain(Player user);
    public abstract void onUseWandSecondary(Player user);
    public abstract String mainAbility();
    public abstract String secondaryAbility();
    public abstract int getMainCooldown(Player user);
    public abstract int getSecondaryCooldown(Player user);

}
