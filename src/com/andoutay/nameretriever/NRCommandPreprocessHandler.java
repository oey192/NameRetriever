package com.andoutay.nameretriever;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class NRCommandPreprocessHandler implements Listener
{
	private NameRetriever plugin;
	
	NRCommandPreprocessHandler(NameRetriever plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent evt)
	{
		String cmd = evt.getMessage();
		String arr[] = cmd.split(" ");


		if (arr.length > 2 && arr[0].matches(NRConfig.cmdRegex2))
		{
			arr[1] = findPlayerForPartial(arr[1]);
			arr[2] = findPlayerForPartial(arr[2]);
			evt.setMessage(implode(arr));
		}
		else if (arr.length > 1 && arr[0].matches(NRConfig.cmdRegex1))
		{
			arr[1] = findPlayerForPartial(arr[1]);
			evt.setMessage(implode(arr));
		}
	}

	private String findPlayerForPartial(String partial)
	{
		String ans = "";
		boolean found = false, foundMult = false;
		for (Player p: plugin.getServer().getOnlinePlayers())
			if (p.getDisplayName().toLowerCase().contains(partial.toLowerCase()))
			{
				ans = p.getName();
				if (!found)
					found = true;
				else
				{
					foundMult = true;
					break;
				}
			}
		
		//if not found, loop through and check p.getName().toLowerCase
		if (!found)
			for (Player p: plugin.getServer().getOnlinePlayers())
				if (p.getName().toLowerCase().contains(partial.toLowerCase()))
				{
					ans = p.getName();
					if (!found)
						found = true;
					else
					{
						foundMult = true;
						break;
					}
				}
		
		if (foundMult || !found)
			ans = partial;
		
		return ans;
	}
	
	private String implode(String[] arr)
	{
		String ans = "";
		for (String s : arr)
			ans += s + " ";
		
		ans = ans.substring(0, ans.length() - 1);
		
		return ans;
	}
}
