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

	public void setHome(CommandSender sender){
		Location pLoc = ((Player)sender).getLocation();
		Spawnr.userprop.setDouble("x", pLoc.getX());
		Spawnr.userprop.setDouble("y", pLoc.getY());
		Spawnr.userprop.setDouble("z", pLoc.getZ());
		Spawnr.userprop.setFloat("yaw", pLoc.getYaw());
		((Player)sender).sendMessage("Player Home set.");
	}

	public void tpHome(CommandSender sender){
		Location locT = ((Player)sender).getLocation();
		locT.setX(Spawnr.userprop.getDouble("x"));
		locT.setY(Spawnr.userprop.getDouble("y"));
		locT.setZ(Spawnr.userprop.getDouble("z"));
		locT.setYaw(Spawnr.userprop.getFloat("yaw"));
		((Player)sender).teleport(locT);
		((Player)sender).sendMessage("Teleported Home.");
	}
}
