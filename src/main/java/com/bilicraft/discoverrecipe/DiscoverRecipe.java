package com.bilicraft.discoverrecipe;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public final class DiscoverRecipe extends JavaPlugin implements Listener {

    private Collection<Recipe<?>> recipesCache;

    @Override
    public void onEnable() {
        // Plugin startup logic
        updateRecipesCache();
        Bukkit.getOnlinePlayers().forEach(this::discoverRecipe);
        Bukkit.getPluginManager().registerEvents(this,this);
        new BukkitRunnable(){
            @Override
            public void run() {
                updateRecipesCache();
            }
        }.runTask(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void updateRecipesCache(){
        this.recipesCache = MinecraftServer.getServer().getRecipeManager().getRecipes();
    }


    private void discoverRecipe(Player player) {
        int unlocked = ((CraftPlayer) player).getHandle().awardRecipes(recipesCache);
        if (unlocked != 0)
            getLogger().info("Unlocked " + unlocked + " recipes for player " + player.getName());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void playerJoin(PlayerJoinEvent event) {
        discoverRecipe(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void pluginLoaded(PluginEnableEvent event) {
        updateRecipesCache();
        Bukkit.getOnlinePlayers().forEach(this::discoverRecipe);
    }
}
