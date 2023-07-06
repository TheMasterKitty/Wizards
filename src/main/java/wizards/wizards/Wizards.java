package wizards.wizards;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import wizards.wizards.commands.GiveWandCommand;
import wizards.wizards.misc.Events;
import wizards.wizards.misc.Utils;
import wizards.wizards.wands.*;
import wizards.wizards.wands.Void;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Wizards extends JavaPlugin {
    public static final Map<String, Wand> wands = new HashMap<>();
    public static final Map<String, ItemStack> wandItems = new HashMap<>();
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(this.getCommand("givewand")).setExecutor(new GiveWandCommand());
        Objects.requireNonNull(this.getCommand("givewand")).setTabCompleter(new GiveWandCommand());

        wands.put("Flaming", new Flaming());
        wands.put("Void", new Void());
        wands.put("Transport", new Transport());
        wands.put("Explosive", new Explosive());
        wands.put("Toxic", new Toxic());

        ItemStack flamingWand = new ItemStack(Material.STICK);
        NBTItem flamingWandNBTI = new NBTItem(flamingWand);
        flamingWandNBTI.setString("wand", "Flaming");
        flamingWandNBTI.applyNBT(flamingWand);
        ItemMeta flamingWandMeta = flamingWand.getItemMeta();
        flamingWandMeta.setDisplayName(Utils.colored("&6Flaming Wand"));
        flamingWandMeta.setLore(Arrays.asList(Utils.colored("&6Main Ability: Fireblast"), Utils.colored("&dLaunches a fireball"), Utils.colored("&6Secondary Ability: Flame Ring"), Utils.colored("&dSummons a ring of fire that lights all near mobs on fire")));
        flamingWand.setItemMeta(flamingWandMeta);
        ShapedRecipe flamingWandRecipe = new ShapedRecipe(flamingWand);
        flamingWandRecipe.shape(" R ", "RHR", " R ");
        flamingWandRecipe.setIngredient('R', Material.BLAZE_ROD);
        flamingWandRecipe.setIngredient('H', Material.PLAYER_HEAD);

        ItemStack voidWand = new ItemStack(Material.STICK);
        NBTItem voidWandNBTI = new NBTItem(voidWand);
        voidWandNBTI.setString("wand", "Void");
        voidWandNBTI.applyNBT(voidWand);
        ItemMeta voidWandMeta = voidWand.getItemMeta();
        voidWandMeta.setDisplayName(Utils.colored("&5Void Wand"));
        voidWandMeta.setLore(Arrays.asList(Utils.colored("&6Main Ability: Pearl"), Utils.colored("&dThrows a pearl (free of charge)"), Utils.colored("&6Secondary Ability: Random Tele-portal"), Utils.colored("&dRandomly teleports you around the world in a 1000x1000 block radius")));
        voidWand.setItemMeta(voidWandMeta);
        ShapedRecipe voidWandRecipe = new ShapedRecipe(voidWand);
        voidWandRecipe.shape(" P ", "PHP", " P ");
        voidWandRecipe.setIngredient('P', Material.ENDER_PEARL);
        voidWandRecipe.setIngredient('H', Material.PLAYER_HEAD);

        ItemStack transportWand = new ItemStack(Material.STICK);
        NBTItem transportWandNBTI = new NBTItem(transportWand);
        transportWandNBTI.setString("wand", "Transport");
        transportWandNBTI.applyNBT(transportWand);
        ItemMeta transportWandMeta = transportWand.getItemMeta();
        transportWandMeta.setDisplayName(Utils.colored("&eTransport Wand"));
        transportWandMeta.setLore(Arrays.asList(Utils.colored("&6Main Ability: Wind Tunnel"), Utils.colored("&dBoost your elyra!"), Utils.colored("&6Secondary Ability: Leash"), Utils.colored("&dAttach a mob to you so you can travel with it easily.")));
        transportWand.setItemMeta(transportWandMeta);
        ShapedRecipe transportWandRecipe = new ShapedRecipe(transportWand);
        transportWandRecipe.shape(" L ", "LHL", " L ");
        transportWandRecipe.setIngredient('L', Material.LEAD);
        transportWandRecipe.setIngredient('H', Material.PLAYER_HEAD);

        ItemStack explosiveWand = new ItemStack(Material.STICK);
        NBTItem explosiveWandNBTI = new NBTItem(explosiveWand);
        explosiveWandNBTI.setString("wand", "Explosive");
        explosiveWandNBTI.applyNBT(explosiveWand);
        ItemMeta explosiveWandMeta = explosiveWand.getItemMeta();
        explosiveWandMeta.setDisplayName(Utils.colored("&eExplosive Wand"));
        explosiveWandMeta.setLore(Arrays.asList(Utils.colored("&6Main Ability: Tactical Missile"), Utils.colored("&dSummons TNT where you are looking"), Utils.colored("&6Secondary Ability: TNT Tunnel"), Utils.colored("&dTNT digs underground in a tunnel and gives you a button to ignite it")));
        explosiveWand.setItemMeta(explosiveWandMeta);
        ShapedRecipe explosiveWandRecipe = new ShapedRecipe(explosiveWand);
        explosiveWandRecipe.shape(" T ", "THT", " T ");
        explosiveWandRecipe.setIngredient('T', Material.TNT);
        explosiveWandRecipe.setIngredient('H', Material.PLAYER_HEAD);

        ItemStack toxicWand = new ItemStack(Material.STICK);
        NBTItem toxicWandNBTI = new NBTItem(toxicWand);
        toxicWandNBTI.setString("wand", "Toxic");
        toxicWandNBTI.applyNBT(toxicWand);
        ItemMeta toxicWandMeta = toxicWand.getItemMeta();
        toxicWandMeta.setDisplayName(Utils.colored("&2Toxic Wand"));
        toxicWandMeta.setLore(Arrays.asList(Utils.colored("&6Main Ability: Toxic Spray"), Utils.colored("&dPoisons the mob you're looking at"), Utils.colored("&6Secondary Ability: Poison Gas"), Utils.colored("&dCreates a cloud of poison that damages nearby players")));
        toxicWand.setItemMeta(toxicWandMeta);
        ShapedRecipe toxicWandRecipe = new ShapedRecipe(toxicWand);
        toxicWandRecipe.shape(" G ", "GHG", " G ");
        toxicWandRecipe.setIngredient('G', Material.GREEN_WOOL);
        toxicWandRecipe.setIngredient('H', Material.PLAYER_HEAD);

        Bukkit.getServer().addRecipe(flamingWandRecipe);
        Bukkit.getServer().addRecipe(voidWandRecipe);
        Bukkit.getServer().addRecipe(transportWandRecipe);
        Bukkit.getServer().addRecipe(explosiveWandRecipe);
        Bukkit.getServer().addRecipe(toxicWandRecipe);

        wandItems.put("Flaming", flamingWand);
        wandItems.put("Void", voidWand);
        wandItems.put("Transport", transportWand);
        wandItems.put("Explosive", explosiveWand);
        wandItems.put("Toxic", toxicWand);
    }

    @Override
    public void onDisable() {

    }
}
