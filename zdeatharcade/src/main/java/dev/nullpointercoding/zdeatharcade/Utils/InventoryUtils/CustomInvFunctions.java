package dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerTextures;

import com.destroystokyo.paper.profile.PlayerProfile;

import net.kyori.adventure.text.Component;

public class CustomInvFunctions {

    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the
                                                                                                     // same "random"
                                                                                                     // UUID all the
                                                                                                     // time

    public static PlayerProfile getProfile(String url) {
        com.destroystokyo.paper.profile.PlayerProfile profile = (com.destroystokyo.paper.profile.PlayerProfile) Bukkit
                .createProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = URL.of(java.net.URI.create(url), null); // The URL to the skin, for example:
                                                               // https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

    public static ItemStack getBackButton() {
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta meta = back.getItemMeta();
        meta.displayName(Component.text("§c§lBACK"));
        back.setItemMeta(meta);
        return back;
    }
}
