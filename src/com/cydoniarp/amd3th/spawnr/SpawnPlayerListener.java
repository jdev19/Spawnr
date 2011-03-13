package com.cydoniarp.amd3th.spawnr;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnPlayerListener extends PlayerListener {
	protected Spawnr plugin;
	
	public SpawnPlayerListener(Spawnr plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerEvent event) {
		Player player = event.getPlayer();
		long worldId = player.getWorld().getId();
		if (!(new File(plugin.pf + "/" + worldId).isDirectory())){
			(new File(plugin.pf + "/" + worldId)).mkdir();
		}
		Spawnr.properties = new Property(plugin.pf + "/" + worldId + "/world.spawn", plugin);
		Spawnr.users = new Property(plugin.pf + "/" + worldId + "/users.spawn", plugin);
		if ((new File(plugin.pf + "/" + worldId + ".spawn")).exists()){
			Property oldWorld = new Property(plugin.pf + "/" + worldId + ".spawn", plugin);
			if (oldWorld.keyExists("x")){
				double oX = oldWorld.getDouble("x");
				double oY = oldWorld.getDouble("y");
				double oZ = oldWorld.getDouble("z");
				float oYaw = oldWorld.getFloat("yaw");
				Spawnr.properties.setDouble("x", oX);
				Spawnr.properties.setDouble("y", oY);
				Spawnr.properties.setDouble("z", oZ);
				Spawnr.properties.setFloat("yaw", oYaw);
				oldWorld.remove("x");
				oldWorld.remove("y");
				oldWorld.remove("z");
				oldWorld.remove("yaw");
			}
			if (oldWorld.keyExists(player.getName())){
				Spawnr.users.setBoolean(player.getName(), true);
				oldWorld.remove(player.getName());
			}
			if(oldWorld.isEmpty()){
				new File(plugin.pf + "/" + worldId + ".spawn").delete();
			}
		}
		if (!Spawnr.properties.keyExists("x")) {
			if(player.isOp()) {
				player.sendMessage("Spawnr point needs to be set.");
			}else{
				Spawnr.properties.setString("x", "");
				Spawnr.properties.setString("y", "");
				Spawnr.properties.setString("z", "");
				Spawnr.properties.setString("yaw", "");
				Location oLoc = player.getWorld().getSpawnLocation();
				player.teleportTo(oLoc);
			}
		}
		else if (!Spawnr.users.keyExists(player.getName())) {
			Location loc = player.getLocation();
			loc.setX(Spawnr.properties.getDouble("x"));
			loc.setY(Spawnr.properties.getDouble("y"));
			loc.setZ(Spawnr.properties.getDouble("z"));
			loc.setYaw(Spawnr.properties.getFloat("yaw"));
			player.teleportTo(loc);
			Spawnr.users.setBoolean(player.getName(), true);
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		long worldId = player.getWorld().getId();
		Spawnr.properties = new Property(plugin.pf + "/" + worldId + "/world.spawn", plugin);
		if (Spawnr.properties.keyExists("x")){
			Location loc = player.getLocation();
			loc.setX(Spawnr.properties.getDouble("x"));
			loc.setY(Spawnr.properties.getDouble("y"));
			loc.setZ(Spawnr.properties.getDouble("z"));
			loc.setYaw(Spawnr.properties.getFloat("yaw"));
			event.setRespawnLocation(loc);
		}
		else {
			Location oLoc = player.getWorld().getSpawnLocation();
			player.teleportTo(oLoc);
			if(player.isOp()){
				player.sendMessage("Spawnr point needs to be set.");
			}
		}
	}
}
