package com.andoutay.nameretriever;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NameRetriever extends JavaPlugin
{
	public static Logger log = Logger.getLogger("Minecraft");
	public static String chPref = ChatColor.DARK_AQUA + "[NameRetriever] " + ChatColor.RESET;
	public static String logPref = "[NameRetriever] ";
	private NREssentialsManager em;
	public static boolean essPresent;
	
	public void onLoad()
	{
		em = new NREssentialsManager(this);
		new NRConfig(this);
	}
	
	public void onEnable()
	{
		essPresent = (getServer().getPluginManager().getPlugin("Essentials") != null);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new NRCommandPreprocessHandler(this), this);
		
		NRConfig.onEnable();
		em.onEnable();
		
		log.info(logPref + "enabled successfully");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (isName(cmd.getName(), args))
			return findName(sender, args);
		else if (isNick(cmd.getName(), args))
			return findNick(sender, args);
		else if (isReload(cmd.getName(), args))
			return reloadConfig(sender);
		else if (isVersion(cmd.getName(), args))
			return showVersion(sender);
		
		return false;
	}
	
	public void onDisable()
	{
		log.info(logPref + "disabled successfully");
	}
	
	private boolean isName(String name, String[] args)
	{
		return name.equalsIgnoreCase("realname") && args.length == 1;
	}
	
	private boolean isNick(String name, String[] args)
	{
		return name.equalsIgnoreCase("realnick") && args.length == 1;
	}
	
	private boolean isReload(String name, String[] args)
	{
		return name.equalsIgnoreCase("nameretriever") && args.length == 1 && args[0].equalsIgnoreCase("reload");
	}
	
	private boolean isVersion(String name, String[] args)
	{
		return name.equalsIgnoreCase("nameretriever") && args.length == 1 && args[0].equalsIgnoreCase("version");
	}
	
	private boolean findName(final CommandSender s, final String[] args)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.realname"))))
			return noAccess(s);

		em.getName(args[0], s);

		return true;
	}

	private boolean findNick(final CommandSender s, final String[] args)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.realnick"))))
			return noAccess(s);

		em.getNickname(args[0], s);

		return true;
	}
	
	private boolean reloadConfig(CommandSender s)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.reload"))))
			return noAccess(s);
		
		NRConfig.reload();
		s.sendMessage(chPref + "Config reloaded");
		
		return true;
	}
	
	private boolean showVersion(CommandSender s)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.version"))))
			return noAccess(s);
		
		s.sendMessage(chPref + "Current version: " + getDescription().getVersion());
		return true;
	}

	private boolean noAccess(CommandSender s)
	{
		s.sendMessage(ChatColor.RED + "You do not have access to that command");
		return true;
	}
}
