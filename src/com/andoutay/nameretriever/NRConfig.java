package com.andoutay.nameretriever;

import org.bukkit.configuration.Configuration;

public class NRConfig
{
	private static Configuration config;
	
	public static boolean showStatus, getOfflineAlways;
	public static String cmdRegex1, cmdRegex2;
	private static NameRetriever plugin;
	
	NRConfig(NameRetriever plugin)
	{
		NRConfig.plugin = plugin;
		config = plugin.getConfig().getRoot();
		//if (config.getString("stopSignal") == null || config.getString("monitorOnLaunch") == null);
			config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public static void onEnable()
	{
		loadConfigVals();
	}
	
	public static void reload()
	{
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();
		onEnable();
	}
	
	private static void loadConfigVals()
	{
		showStatus = config.getBoolean("showStatus");
		getOfflineAlways = config.getBoolean("getOfflineAlways");
		cmdRegex1 = config.getString("cmdRegex1");
		cmdRegex2 = config.getString("cmdRegex2");
	}

}
