package com.cydoniarp.amd3th.spawnr;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Spawnr extends JavaPlugin {
	public static PermissionHandler Permissions;
	private final SpawnPlayerListener pListener = new SpawnPlayerListener(this);
	public Logger log = Logger.getLogger("Minecraft");
	public String pName;
	public static Property world;
	public static Property users = null;
	public static Property userprop;
	public static Property userspawnprop;
	private Property config = null;
	public long worldID;
	public String pf = null;
	private sCom worker;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.pListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, this.pListener, Event.Priority.Normal, this);
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
				config.setBoolean("OPonlyHomes", false);
			}else{
				config = new Property(this.getDataFolder()+"/config.txt", this);
			}
		}
		final Plugin perm = pm.getPlugin("Permissions");
		setupPermissions();
		this.worker = new sCom(this);
		getCommand("spawnr").setExecutor(new CommandExecutor(){
			public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
				String cmdName = cmd.getName();
				if (sender instanceof Player){
					if (cmdName.equalsIgnoreCase("spawnr")){
						if (args.length == 1) {
							//TELEPORT COMMAND
							if (args[0].equalsIgnoreCase("tp")){
								if(perm != null){
									if(Spawnr.Permissions.has(((Player)sender), "spawnr.tp")){
										//COMMAND FUNCTION
										if(Spawnr.world.keyExists("x")){
											Spawnr.this.worker.globalTp(sender);
											return true;
										}else{
											((Player)sender).sendMessage(ChatColor.RED+"No spawn point is set.");
											return true;
										}
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}else{
									if(((Player)sender).isOp() || (!Spawnr.this.config.getBoolean("OPonlyTeleport"))){
										if(Spawnr.world.keyExists("x")){
											Spawnr.this.worker.globalTp(sender);
										}else{
											((Player)sender).sendMessage(ChatColor.RED+"No spawn point is set.");
										}
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}
							}
							//SET SPAWN COMMAND	
							if (args[0].equalsIgnoreCase("set")) {
								if(perm != null){
									if(Spawnr.Permissions.has(((Player)sender), "spawnr.set")){
										//COMMAND FUNCTION
										Spawnr.this.worker.globalSet(sender);
										return true;
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}else{
									if(((Player)sender).isOp()){
										//COMMAND FUNCTION
										Spawnr.this.worker.globalSet(sender);
										return true;
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}
							}
							//SET HOME COMMAND
							if (args[0].equalsIgnoreCase("sethome")){
								if(perm != null){
									if(Spawnr.Permissions.has(((Player)sender), "spawnr.sethome")){
										//COMMAND FUNCTION
										Spawnr.this.worker.setHome(sender);
										return true;
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}else{
									if(((Player)sender).isOp() || (!Spawnr.this.config.getBoolean("OPonlyHomes"))){
										//COMMAND FUNCTION
										Spawnr.this.worker.setHome(sender);
										return true;
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}
							}
							//TP HOME COMMAND
							if (args[0].equalsIgnoreCase("home")){
								if(perm != null){
									if(Spawnr.Permissions.has(((Player)sender), "spawnr.home")){
										if(Spawnr.userprop.keyExists("x")){
											Spawnr.this.worker.tpHome(sender);
											return true;
										}else{
											((Player)sender).sendMessage(ChatColor.RED+"You don't have a home.");
											return true;
										}
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}else{
									if(((Player)sender).isOp() || (!Spawnr.this.config.getBoolean("OPonlyHomes"))){
										if(Spawnr.userprop.keyExists("x")){
											Spawnr.this.worker.tpHome(sender);
											return true;
										}else{
											((Player)sender).sendMessage(ChatColor.RED+"You don't have a home.");
											return true;
										}
									}else{
										((Player)sender).sendMessage(ChatColor.RED+"You don't have permission to use that command.");
										return true;
									}
								}
							}
						}
					}
				}
				return false;
			}
		});
	}

	private void setupPermissions(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdf = this.getDescription();
		if (Spawnr.Permissions == null) {
			if (test != null) {
				Spawnr.Permissions = ((Permissions)test).getHandler();
			} else {
				log.info("[" + pName + "] v" + pdf.getVersion() + ": Permissions system not detected, defaulting to OP.");
			}
		}
	}

	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		log.info("[" + pName + "] v" + pdf.getVersion() + " has been disabled.");




	}
}