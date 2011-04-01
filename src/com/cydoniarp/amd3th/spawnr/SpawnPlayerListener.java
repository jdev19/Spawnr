package com.cydoniarp.amd3th.spawnr;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnPlayerListener extends PlayerListener {
	protected Spawnr plugin;
	
	public SpawnPlayerListener(Spawnr plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String plName = player.getName();
		long worldId = player.getWorld().getId();
		if (!(new File(plugin.pf+"/"+worldId).isDirectory())){
			(new File(plugin.pf+"/"+worldId)).mkdir();
		}
		if (!(new File(plugin.pf+"/"+worldId+"/"+plName).isDirectory())){
			(new File(plugin.pf+"/"+worldId+"/"+plName)).mkdir();
		}
		Spawnr.world = new Property(plugin.pf + "/" + worldId + "/world.spawn", plugin);
		Spawnr.users = new Property(plugin.pf + "/" + worldId + "/users.spawn", plugin);
		Spawnr.userspawnprop = new Property(plugin.pf + "/" + worldId + "/" + plName + "/pl.spawn", plugin);
		Spawnr.userprop = new Property(plugin.pf+"/"+worldId+"/"+plName+"/home.spawn", plugin);
		
		if ((Spawnr.world.keyExists("x")) && (!Spawnr.users.keyExists(plName))){
			Location loc = player.getLocation();
			loc.setX(Spawnr.world.getDouble("x"));
			loc.setY(Spawnr.world.getDouble("y"));
			loc.setZ(Spawnr.world.getDouble("z"));
			loc.setYaw(Spawnr.world.getFloat("yaw"));
			player.teleport(loc);
		}
		else if((!Spawnr.world.keyExists("x") && (!Spawnr.users.keyExists(plName)))){
			Location oLoc = player.getWorld().getSpawnLocation();
			player.teleport(oLoc);
			if(player.isOp() || (Spawnr.Permissions.has(player, "spawnr.set"))){
				player.sendMessage("Spawnr point needs to be set.");
			}
		}
		
		if(Spawnr.users.keyExists(player.getName())){
			Location uLoc = player.getLocation();
			uLoc.setX(Spawnr.userspawnprop.getDouble("x"));
			uLoc.setY(Spawnr.userspawnprop.getDouble("y"));
			uLoc.setZ(Spawnr.userspawnprop.getDouble("z"));
			uLoc.setYaw(Spawnr.userspawnprop.getFloat("yaw"));
			player.teleport(uLoc);
		}else{
			Location loc = player.getLocation();
			loc.setX(Spawnr.world.getDouble("x"));
			loc.setY(Spawnr.world.getDouble("y"));
			loc.setZ(Spawnr.world.getDouble("z"));
			loc.setYaw(Spawnr.world.getFloat("yaw"));
			player.teleport(loc);
			Spawnr.users.setBoolean(player.getName(), true);
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Spawnr.world.keyExists("x")){
			Location loc = player.getLocation();
			loc.setX(Spawnr.world.getDouble("x"));
			loc.setY(Spawnr.world.getDouble("y"));
			loc.setZ(Spawnr.world.getDouble("z"));
			loc.setYaw(Spawnr.world.getFloat("yaw"));
			event.setRespawnLocation(loc);
		}
		else {
			Location oLoc = player.getWorld().getSpawnLocation();
			player.teleport(oLoc);
			if(player.isOp()){
				player.sendMessage("Spawnr point needs to be set.");
			}
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Location pLoc = player.getLocation();
		Spawnr.userspawnprop.setDouble("x", pLoc.getX());
		Spawnr.userspawnprop.setDouble("y", pLoc.getY());
		Spawnr.userspawnprop.setDouble("z", pLoc.getZ());
		Spawnr.userspawnprop.setFloat("yaw", pLoc.getYaw());
	}
}
