package com.andoutay.nameretriever;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

public class NREssentialsManager
{
	public static Logger log = Logger.getLogger("Minecraft");
	NameRetriever plugin;
	private Essentials e;
	
	NREssentialsManager(NameRetriever plugin)
	{
		this.plugin = plugin;
	}
	
	public void onEnable()
	{
		if (NameRetriever.essPresent) e = (Essentials)plugin.getServer().getPluginManager().getPlugin("Essentials");
	}
	
	public void getName(final String name, final CommandSender s)
	{
		HashMap<String, String> ans = new HashMap<String, String>();
		
		//try to get name based on partial match from online players - just in case Essentials hasn't saved a file for an online player
		for (Player p: plugin.getServer().getOnlinePlayers())
			if (p.getDisplayName().toLowerCase().contains(name.toLowerCase()))
				ans.put(p.getName(), p.getDisplayName());

		if (NameRetriever.essPresent && (NRConfig.getOfflineAlways || ans.isEmpty()))
		{
			final HashMap<String, String> tempAns = ans;
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				public void run() {
					HashMap<String, String> offlineAns = tempAns;
					//try to get name from Essential's <username>.yml files - works if player has played on the server before and essentials files have not been tampered with
					File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
					if (files.length == 1)
					{
						File f = files[0];
						for (File yml : f.listFiles())
							if (yml.getName().endsWith(".yml"))
							{
								String temp = YamlConfiguration.loadConfiguration(yml).getString("nickname");
								if (temp != null && temp.toLowerCase().contains(name.toLowerCase()))
									offlineAns.put(plugin.getServer().getOfflinePlayer(yml.getName().substring(0, yml.getName().length() - 4)).getName(), "~" + temp);
							}
					}
					
					tellName(offlineAns, s, name);
				}});
		}
		else
			tellName(ans, s, name);
	}

	public void getNickname(final String name, final CommandSender s)
	{
		HashMap<String, String> ans = new HashMap<String, String>();

		//try to get nick based on partial match from online players - just in case essentials hasn't saved a file for an online player
		for (Player player : plugin.getServer().getOnlinePlayers())
			if (player.getName().toLowerCase().contains(name.toLowerCase()))
				ans.put(player.getName(), player.getDisplayName());

		if (NameRetriever.essPresent && (NRConfig.getOfflineAlways || ans.isEmpty()))
		{
			final HashMap<String, String> tempAns = ans;
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				public void run() {
					HashMap<String, String> offlineAns = tempAns;
					//try to get nick from Essential's <username>.yml files - works if player has played on the server before and essentials files have not been tampered with
					File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
					if (files.length == 1)
					{
						File f = files[0];
						for (File yml : f.listFiles())
							if (yml.getName().endsWith(".yml"))
							{
								String fname = yml.getName().substring(0, yml.getName().length() - 4);
								if (fname.toLowerCase().contains(name.toLowerCase()))
								{
									String temp1, temp2;

									temp1 = plugin.getServer().getOfflinePlayer(fname).getName();
									temp2 = YamlConfiguration.loadConfiguration(yml).getString("nickname");
									if (temp2 == null)
										temp2 = temp1;
									else
										temp2 = "~" + temp2;

									offlineAns.put(temp1, temp2);
								}
							}
						tellNick(offlineAns, s);
					}
				}});
		}
		else
			tellNick(ans, s);
	}

	private void tellName(HashMap<String, String> names, CommandSender s, String name)
	{
		if (names.isEmpty())
			//will display the error message if no player found
			getNickname(name, s);
		else
			for (String str : names.keySet())
			{
				s.sendMessage(names.get(str) + " is " + str);
				if (NRConfig.showStatus && NameRetriever.essPresent) s.sendMessage("Status: " + ((plugin.getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
			}
	}
	
	private void tellNick(HashMap<String, String> names, CommandSender s)
	{
		if (names.isEmpty())
			s.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Player not found");
		else
			for(String str : names.keySet())
			{
				s.sendMessage(str + "'s nickname is " + names.get(str));
				if (NRConfig.showStatus && NameRetriever.essPresent) s.sendMessage("Status: " + ((plugin.getServer().getPlayerExact(str) == null) ? (ChatColor.RED + "Offline") : (ChatColor.GREEN + "Online")));
			}
	}
}
