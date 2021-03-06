package com.andoutay.nameretriever;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
	public static String logPref = "[NameRetriever] ";
	public static String chPref = ChatColor.DARK_AQUA + logPref + ChatColor.RESET;
	private static NREssentialsManager em;
	public static boolean essPresent;
	
	public void onLoad()
	{
		em = new NREssentialsManager(this);
		new NRConfig(this);
	}
	
	public void onEnable()
	{
		essPresent = (getServer().getPluginManager().getPlugin("Essentials") != null);
		if (!essPresent) log.warning(logPref + "Essentials not found. This plugin will have limited functionality");
		
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
		else if (isHelp(cmd.getName(), args))
			return showHelp(sender, label);
		else if (isNameButWrongArgs(cmd.getName(), args))
		{
			sender.sendMessage(cmd.getUsage().replace("<command>", label));
			return true;
		}
		
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
	
	private boolean isNameButWrongArgs(String name, String[] args)
	{
		return name.equalsIgnoreCase("realname") && args.length != 1;
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
	
	private boolean isHelp(String name, String[] args)
	{
		return name.equalsIgnoreCase("nameretriever") && args.length == 1 && args[0].equalsIgnoreCase("help");
	}
	
	private boolean findName(final CommandSender s, final String[] args)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.realname"))))
			return noAccess(s);

		final Future<HashMap<String, String>> returnFuture = getServer().getScheduler().callSyncMethod(this, new Callable<HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call()
			{
				return em.getNamesForNick(args[0]);					
			}
		});
		
		final Future<HashMap<String, String>> returnFuture2 = getServer().getScheduler().callSyncMethod(this, new Callable<HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call()
			{
				return em.getNicksForName(args[0]);					
			}
		});
		
		if (NRConfig.useAsync)
		{
			getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
				@Override
				public void run()
				{
					boolean fnick = false;
					try {
						HashMap<String, String> names = returnFuture.get();

						if (names.isEmpty())
						{
							names = returnFuture2.get();
							fnick = true;
						}
						
						if (names.isEmpty())
							s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "No player found with that nickname");
						else
							for (String str : names.keySet())
							{
								if (fnick)
									s.sendMessage(str + "'s nickname is " + names.get(str));
								else
									s.sendMessage(names.get(str) + " is " + str);
								if (NRConfig.showStatus && NameRetriever.essPresent) s.sendMessage("Status: " + ((getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
							}

					} catch (InterruptedException e) {
						s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Unexpected interruption while fetching results");
					} catch (ExecutionException e) {
						s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Unexpected execution error while fetching results");
					}
				}
			});
		}
		else
		{
			boolean nick = false;
			HashMap<String, String> names = em.getNamesForNick(args[0]);
			if (names.isEmpty())
			{
				names = em.getNicksForName(args[0]);
				nick = true;
			}

			if (names.isEmpty())
				s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "No player found with that nickname");
			else
				for (String str : names.keySet())
				{
					if (nick)
						s.sendMessage(str + "'s nickname is " + names.get(str));
					else
						s.sendMessage(names.get(str) + " is " + str);
					if (NRConfig.showStatus && NameRetriever.essPresent) s.sendMessage("Status: " + ((getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
				}
		}

		return true;
	}

	private boolean findNick(final CommandSender s, final String[] args)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.realnick"))))
			return noAccess(s);

		final Future<HashMap<String, String>> returnFuture = getServer().getScheduler().callSyncMethod(this, new Callable<HashMap<String, String>>() {
			@Override
			public HashMap<String, String> call()
			{
				return em.getNicksForName(args[0]);					
			}
		});
		
		if (NRConfig.useAsync)
		{
			getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
				@Override
				public void run()
				{
					try {
						HashMap<String, String> nicks = returnFuture.get();

						if (nicks.isEmpty())
							s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Player not found");
						else
							for(String str : nicks.keySet())
							{
								s.sendMessage(str + "'s nickname is " + nicks.get(str));
								if (NRConfig.showStatus) s.sendMessage("Status: " + ((getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
							}

					} catch (InterruptedException e) {
						s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Unexpected interruption while fetching results");
					} catch (ExecutionException e) {
						s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Unexpected execution error while fetching results");
					}
				}
			});
		}
		else
		{
			HashMap<String, String> nicks = em.getNicksForName(args[0]);

			if (nicks.isEmpty())
				s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Player not found");
			else
				for(String str : nicks.keySet())
				{
					s.sendMessage(str + "'s nickname is " + nicks.get(str));
					if (NRConfig.showStatus) s.sendMessage("Status: " + ((getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
				}	
		}

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
	
	private boolean showHelp(CommandSender s, String label)
	{
		if (!(s instanceof ConsoleCommandSender || (s instanceof Player && ((Player)s).hasPermission("nameretriever.help"))))
			return noAccess(s);
		
		s.sendMessage(chPref + "Help:");
		s.sendMessage("/" + label + " version: Show the current plugin version");
		s.sendMessage("/" + label + " reload: Reload the config file");
		s.sendMessage("/" + label + " help: Show this help message");
		s.sendMessage("/realname <nickname>: Get the Minecraft username for a given nickname");
		s.sendMessage("      Aliases: /rname, /name");
		s.sendMessage("/realnick <username>: Get the nickname for a given Minecraft username");
		s.sendMessage("      Alias: /rnick");
		return true;
	}

	private boolean noAccess(CommandSender s)
	{
		s.sendMessage(ChatColor.RED + "You do not have access to that command");
		return true;
	}
	
	public static NREssentialsManager getEssManager()
	{
		return em;
	}
}
