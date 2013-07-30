package com.andoutay.nameretriever;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.logging.Logger;

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
	
	private HashMap<String, String> getOnlineNamesForNick(String partial)
	{
		HashMap<String, String> ans = new HashMap<String, String>();
		
		for (Player p: plugin.getServer().getOnlinePlayers())
			if (p.getDisplayName().toLowerCase().contains(partial.toLowerCase()) && !p.getName().equals(p.getDisplayName()))
				ans.put(p.getName(), p.getDisplayName());
		
		return ans;
	}
	
	private HashMap<String, String> getOfflineNamesForNick(String partial)
	{
		HashMap<String, String> ans = new HashMap<String, String>();
		
		//We need Essentials to get offline nicknames so if it's not loaded, just return an empty HashMap
		if (!NameRetriever.essPresent) return ans;
		
		File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
		if (files.length == 1)
		{
			File f = files[0];
			for (File yml : f.listFiles())
				if (yml.getName().endsWith(".yml"))
				{
					String temp = YamlConfiguration.loadConfiguration(yml).getString("nickname");
					if (temp != null && temp.toLowerCase().contains(partial.toLowerCase()))
						ans.put(plugin.getServer().getOfflinePlayer(yml.getName().substring(0, yml.getName().length() - 4)).getName(), NRConfig.nickIndicatorChar + temp);
				}
		}
		else
			log.warning(NameRetriever.logPref + "Could not find Essentials' userdata folder when attempting to retrieve nicknames");
		
		return ans;
	}
	
	private HashMap<String, String> getOnlineNicksForName(String partial)
	{
		HashMap<String, String> ans = new HashMap<String, String>();

		//try to get nick based on partial match from online players - just in case essentials hasn't saved a file for an online player
		for (Player player : plugin.getServer().getOnlinePlayers())
			if (player.getName().toLowerCase().contains(partial.toLowerCase()))
				ans.put(player.getName(), player.getDisplayName());
		
		return ans;
	}
	
	private HashMap<String, String> getOfflineNicksForName(String partial)
	{
		HashMap<String, String> ans = new HashMap<String, String>();
		
		//We need Essentials to get offline nicknames so if it's not loaded, just return an empty HashMap
		if (!NameRetriever.essPresent) return ans;
		
		File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
		if (files.length == 1)
		{
			File f = files[0];
			for (File yml : f.listFiles())
				if (yml.getName().endsWith(".yml"))
				{
					String fname = yml.getName().substring(0, yml.getName().length() - 4);
					if (fname.toLowerCase().contains(partial.toLowerCase()))
					{
						String temp1, temp2;

						temp1 = plugin.getServer().getOfflinePlayer(fname).getName();
						temp2 = YamlConfiguration.loadConfiguration(yml).getString("nickname");
						if (temp2 == null)
							temp2 = temp1;
						else
							temp2 = NRConfig.nickIndicatorChar + temp2;

						ans.put(temp1, temp2);
					}
				}
		}
		else
			log.warning(NameRetriever.logPref + "Could not find Essentials' userdata folder when attempting to retrieve nicknames");
		
		return ans;
	}
	
	public HashMap<String, String> getOnAndOfflineNamesForNick(String partial)
	{
		HashMap<String, String> ans = getOnlineNamesForNick(partial);
		ans.putAll(getOfflineNamesForNick(partial));
		return ans;
	}
	
	public HashMap<String, String> getOnAndOfflineNicksForName(String partial)
	{
		HashMap<String, String> ans = getOnlineNicksForName(partial);
		ans.putAll(getOfflineNicksForName(partial));
		return ans;
	}
	
	public HashMap<String, String> getNamesForNick(String partial)
	{
		HashMap<String, String> ans = getOnlineNamesForNick(partial);
		if (NRConfig.getOfflineAlways || ans.isEmpty()) ans.putAll(getOfflineNamesForNick(partial));
		return ans;
	}
	
	public HashMap<String, String> getNicksForName(String partial)
	{
		HashMap<String, String> ans = getOnlineNicksForName(partial);
		if (NRConfig.getOfflineAlways || ans.isEmpty()) ans.putAll(getOfflineNicksForName(partial));
		return ans;
	}
}
