package dev.nullpointercoding.zdeatharcade.Vendors.Snacks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Snack {

    private final String name;
    private final Material material;
    private final Component displayName;
    private final PotionEffectType effect;
    private final ItemStack item;
    private final Double feedAmount;
    private final Double worth;
    private final Integer duration;
    private final Integer ampilfer;
    private final Double healAmount;
    private final Float saturation;
    private static final List<Snack> snacks = new ArrayList<>();

    public Snack(String name, Material material, Component dName, PotionEffectType effect, Integer dur, Integer amp,
            Double feedAmount, Double healAmount,Float sat, Double worth) {
        this.name = name;
        this.material = material;
        this.displayName = dName;
        this.effect = effect;
        this.feedAmount = feedAmount;
        this.healAmount = healAmount;
        this.saturation = sat;
        this.worth = worth;
        this.duration = dur;
        this.ampilfer = amp;
        this.item = createItemStack();
        snacks.add(this);
    }

    public String getName() {
        return name;
    }

    private ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Effect: ", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC)
            .append(Component.text(effect.translationKey(), NamedTextColor.WHITE)));
        lore.add(Component.text("Price: ", NamedTextColor.GREEN, TextDecoration.ITALIC)
                .append(Component.text(worth.toString(), NamedTextColor.WHITE)));
        lore.add(Component.text("Feed Amount: ", NamedTextColor.GOLD, TextDecoration.ITALIC)
                .append(Component.text(feedAmount.toString(), NamedTextColor.WHITE)));
        lore.add(Component.text("Heal Amount: ", NamedTextColor.RED, TextDecoration.ITALIC)
                .append(Component.text(healAmount.toString(), NamedTextColor.WHITE,TextDecoration.ITALIC)));
        lore.add(Component.text("Saturation: ", NamedTextColor.AQUA, TextDecoration.ITALIC)
                .append(Component.text(saturation.toString(), NamedTextColor.WHITE,TextDecoration.ITALIC)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSnack() {
        return item;
    }

    public Double getWorth() {
        return worth;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getAmplifier() {
        return ampilfer;
    }

    public Double getFeedAmount() {
        return feedAmount;
    }

    public Double getHealAmount() {
        return healAmount;
    }
    public Float getSaturation(){
        return saturation;
    }

    public static Boolean isSnack(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            Component displayName = meta.displayName();

            // Check if the display name or any other unique identifier
            // matches the snack identifier you have set
            for (Snack snack : snacks) {
                if (displayName != null && snack.getSnack().getItemMeta().displayName().equals(displayName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public PotionEffectType getEffect() {
        return effect;
    }

    public static Snack ItemStackToSnack(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            Component displayName = meta.displayName();
            // Check if the display name or any other unique identifier
            // matches the snack identifier you have set
            for (Snack snack : snacks) {
                if (displayName != null && snack.getSnack().getItemMeta().displayName().equals(displayName)) {
                    return snack;
                }
            }
        }
        return null;

    }

    public ItemStack getSnackItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Right-Click to eat", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void eatSnack(Player eater){
        Snack snack = ItemStackToSnack(eater.getInventory().getItemInMainHand());
        Double pHealth = eater.getHealth();
        Integer pFood = eater.getFoodLevel();
        Float pSat = eater.getSaturation();

        Double newPHealth = pHealth + snack.getHealAmount();
        Integer newPFood = pFood + snack.getFeedAmount().intValue();
        Float newPSat = pSat + snack.getSaturation();
        Double totalLife = eater.getHealth() + eater.getFoodLevel() + eater.getSaturation();
        if(totalLife == 48){
            eater.sendMessage(Component.text("You are already full!",NamedTextColor.RED));
            return;
        }
        if(newPHealth > 20){
            newPHealth = 20.0d;
        }
        if(newPFood > 20){
            newPFood = 20;
        }
        if(newPSat > 8){
            newPSat = 8.00f;
        }

        eater.setHealth(newPHealth);
        eater.setFoodLevel(newPFood);
        eater.setSaturation(newPSat);
        if(eater.getInventory().getItemInMainHand().getAmount() > 1){
            eater.getInventory().getItemInMainHand().setAmount(eater.getInventory().getItemInMainHand().getAmount() - 1);
        }else{
            eater.getInventory().setItemInMainHand(null);
        }

        eater.addPotionEffect(new PotionEffect(snack.getEffect(),snack.getDuration() * 20,snack.getAmplifier()));


    }
}
