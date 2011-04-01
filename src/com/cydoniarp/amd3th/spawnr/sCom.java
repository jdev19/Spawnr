package com.cydoniarp.amd3th.spawnr;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class sCom {
	protected Spawnr plugin;

	public sCom(Spawnr plugin){
		this.plugin = plugin;
	}

	public void globalSet(CommandSender sender){
		Location loc = ((Player)sender).getLocation();
		Spawnr.world.setDouble("x", loc.getX());
		Spawnr.world.setDouble("y", loc.getY());
		Spawnr.world.setDouble("z", loc.getZ());
		Spawnr.world.setFloat("yaw", loc.getYaw());
		((Player)sender).sendMessage("Spawnr point set.");
	}

	public void globalTp(CommandSender sender){
		Location locS = ((Player)sender).getLocation();
		locS.setX(Spawnr.world.getDouble("x"));
		locS.setY(Spawnr.world.getDouble("y"));
		locS.setZ(Spawnr.world.getDouble("z"));
		locS.setYaw(Spawnr.world.getFloat("yaw"));
		((Player)sender).teleport(locS);
		((Player)sender).sendMessage("Teleported!");
	}
}
