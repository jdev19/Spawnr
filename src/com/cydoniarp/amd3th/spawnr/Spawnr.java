package com.cydoniarp.amd3th.spawnr;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawnr extends JavaPlugin {
	private final SpawnPlayerListener pListener = new SpawnPlayerListener(this);
	private static Logger log = Logger.getLogger("Minecraft");
	public String pName;
	public static Property properties;
	public static Property users = null;
	public static Property oldWorld = null;
	private Property config = null;
	public long worldID;
	public String pf = null;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.pListener, Event.Priority.Normal, this);
		PluginDescriptionFile pdf = this.getDescription();
		pName = pdf.getName();
		pf = this.getDataFolder().toString();
		log.info("["+pName+"] v"+pdf.getVersion()+" has been enabled");
		if (!((this.getDataFolder()).isDirectory())) {
			this.getDataFolder().mkdir();
		}
		if (config == null){
			if(!(new File(this.getDataFolder()+"/config.txt").exists())){
				config = new Property(this.getDataFolder()+"/config.txt", this);
				config.setBoolean("OPonlyTeleport", true);
			}else{
				config = new Property(this.getDataFolder()+"/config.txt", this);
			}
		}

		getCommand("spawnr").setExecutor(new CommandExecutor(){
			public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
				String cmdName = cmd.getName();
				if (sender instanceof Player){
					if (cmdName.equalsIgnoreCase("spawnr")){
						//if player is OP or if player config does not have OP only true
						if (((Player)sender).isOp() || !config.getBoolean("OPonlyTeleport")) {
							if (args.length == 1) {
								if (args[0].equalsIgnoreCase("tp")) {
									if (properties.keyExists("x")){
										Location locS = ((Player)sender).getLocation();
										locS.setX(properties.getDouble("x"));
										locS.setY(properties.getDouble("y"));
										locS.setZ(properties.getDouble("z"));
										locS.setYaw(properties.getFloat("yaw"));
										((Player)sender).teleportTo(locS);
										((Player)sender).sendMessage("Teleported!");
										return true;
									}else{
										if (((Player)sender).isOp()){
											((Player)sender).sendMessage("No spawn point is set.");
										}else{
											((Player)sender).sendMessage("Tell an OP to set a spawn point");
										}
										return true;
									}
								}else if (args[0].equalsIgnoreCase("set") && ((Player)sender).isOp()) {
									Location loc = ((Player)sender).getLocation();
									properties.setDouble("x", loc.getX());
									properties.setDouble("y", loc.getY());
									properties.setDouble("z", loc.getZ());
									properties.setFloat("yaw", loc.getYaw());
									((Player)sender).sendMessage("Spawnr point set.");
									return true;
								}
							}
						}
					}
				}
				return false;
			}
		});
	}

		public void onDisable() {
			PluginDescriptionFile pdf = this.getDescription();
			log.info("[" + pName + "] v" + pdf.getVersion() + " has been disabled.");
		}

	}