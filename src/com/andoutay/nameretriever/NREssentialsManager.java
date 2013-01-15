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
	
	public HashMap<String, String> getName(String name)
	{
		HashMap<String, String> ans = new HashMap<String, String>();
		
		//try to get name based on partial match from online players - just in case Essentials hasn't saved a file for an online player
		for (Player p: plugin.getServer().getOnlinePlayers())
			if (p.getDisplayName().toLowerCase().contains(name.toLowerCase()))
				ans.put(p.getName(), p.getDisplayName());

		if (NameRetriever.essPresent)
		{
			//try to get name from Essential's <username>.yml files - works if player has played on the server before and essentials files have not been tampered with
			File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
			if (files.length != 1) return ans;

			File f = files[0];
			for (File yml : f.listFiles())
				if (yml.getName().endsWith(".yml"))
				{
					String temp = YamlConfiguration.loadConfiguration(yml).getString("nickname");
					if (temp != null && temp.toLowerCase().contains(name.toLowerCase()))
						ans.put(plugin.getServer().getOfflinePlayer(yml.getName().substring(0, yml.getName().length() - 4)).getName(), "~" + temp);
				}
			
		}
		
		//perhaps the person put in the person's realname
		if (ans.isEmpty()) ans = getNickname(name);
		
		return ans;
	}

	public HashMap<String, String> getNickname(String name)
	{
		HashMap<String, String> ans = new HashMap<String, String>();

		//try to get nick based on partial match from online players - just in case essentials hasn't saved a file for an online player
		for (Player player : plugin.getServer().getOnlinePlayers())
			if (player.getName().toLowerCase().contains(name.toLowerCase()))
				ans.put(player.getName(), player.getDisplayName());

		if (NameRetriever.essPresent)
		{
			//try to get nick from Essential's <username>.yml files - works if player has played on the server before and essentials files have not been tampered with
			File[] files = e.getDataFolder().listFiles(new FilenameFilter() { public boolean accept(File dir, String filename) { return filename.endsWith("userdata");}});
			if (files.length != 1) return ans;

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

						ans.put(temp1, temp2);
					}
				}
		}
		return ans;
	}
}
