package com.bilicraft.discoverrecipe;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscoverRecipe extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getOnlinePlayers().forEach(player->{
            int unlocked = ((CraftPlayer)player).getHandle().awardRecipes(MinecraftServer.getServer().getRecipeManager().getRecipes());
            if(unlocked != 0)
                getLogger().info("Unlocked "+unlocked+" recipes for player "+player.getName());
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void playerJoin(PlayerJoinEvent event){
       int unlocked = ((CraftPlayer)event.getPlayer()).getHandle().awardRecipes(MinecraftServer.getServer().getRecipeManager().getRecipes());
       if(unlocked != 0)
           getLogger().info("Unlocked "+unlocked+" recipes for player "+event.getPlayer().getName());
    }
}
