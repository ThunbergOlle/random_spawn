package me.bukkit.randomspawn;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;

public class main extends JavaPlugin implements Listener{
	
	
	@Override
	public void onEnable() {
		getLogger().info("Random spawn enabled");
		loadConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();

	    
	    double x = getConfig().getDouble("World."+worldName+".x");
	    double y = getConfig().getDouble("World."+worldName+".y");
	    double z = getConfig().getDouble("World."+worldName+".z");
	    
	    int radius = getConfig().getInt("World."+worldName+".radius");
		
		//MANIPULATE RESPAWN
		x += Math.random() * radius + 1;
		z += Math.random() * radius + 1;

		Location respawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z);

		event.setRespawnLocation(respawnLocation);

	}
	public boolean onCommand(CommandSender sender, Command cmd, String labvel, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("setspawnarea") && sender instanceof Player) {
			
			Player player = (Player) sender;
			if(player.isOp()) {
				if(args.length == 0) {
					player.sendMessage("Please provide a spawn radius");
					return false;
				}	
				String worldName = player.getWorld().getName();
				int radius =  Integer.parseInt(args[0]);

				//Location respawnLocation = new Location(player.getWorld(), Integer.parseInt((String) getConfig().get("World." + worldName + ".area.x")), Integer.parseInt((String)getConfig().get("World." + worldName + ".area.y")), Integer.parseInt((String)getConfig().get("World." + worldName + ".area.z")));	
				getConfig().set("World."+worldName+".x", Double.valueOf(player.getLocation().getX()));
				getConfig().set("World."+worldName+".y", Double.valueOf(player.getLocation().getY()));
				getConfig().set("World."+worldName+".z", Double.valueOf(player.getLocation().getZ()));

				getConfig().set("World."+worldName+".radius", radius);
				saveConfig();
				
				
				
				//PLACE BLOCKS SO YOU SEE IN WHAT AREA THEY CAN SPAWN IN
				Location newSpawnLoc = new Location(Bukkit.getWorld(worldName), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
				
				newSpawnLoc.getBlock().setType(Material.GLASS);
				
				newSpawnLoc.setX(newSpawnLoc.getX() + radius);
				newSpawnLoc.getBlock().setType(Material.GLASS);
				
				newSpawnLoc.setZ(newSpawnLoc.getZ() + radius);
				newSpawnLoc.getBlock().setType(Material.GLASS);
				
				newSpawnLoc.setX(newSpawnLoc.getX() - radius);
				newSpawnLoc.getBlock().setType(Material.GLASS);
				
				player.sendMessage("Set config for: " + worldName + " with spawn radius of " + radius);
				
			}else {
				player.sendMessage("You don't have permission");
			}

		}
		
		return false;
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);	
		saveConfig();
	}
}
