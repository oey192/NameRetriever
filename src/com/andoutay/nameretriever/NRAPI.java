package com.andoutay.nameretriever;

import java.util.HashMap;

public class NRAPI
{
	private static NRAPI api = null;
	private static NREssentialsManager em;
	
	protected NRAPI()
	{
		em = NameRetriever.getEssManager();
	}
	
	/**
	 * Returns the singleton instance of the NRAPI object
	 * for use with the various NameRetriever API methods
	 * 
	 * @return	the singleton instance of the NRAPI object
	 */
	public static NRAPI getAPI()
	{
		if (api == null) api = new NRAPI();
		
		return api;
	}
	
	/**
	 * Returns the nickname associated with the given Minecraft username on the current server.
	 * <br>
	 * If the given player has no nickname, the player's Minecraft username is returned.
	 * <br>
	 * If more than one user with the given username is found, an empty string is returned
	 * <br>
	 * If no user with the given username is found, null is returned.
	 * <br>
	 * This function is disk intensive. It's recommended to make an asynchronous call to it
	 * 
	 * @param 	username	The full username of the player you would like to find a nickname for
	 * @return				the nickname associated with the given Minecraft username
	 * @see					NRAPI#getNicks(String) getNicks(String)
	 */
	public String getNick(String username)
	{
		String ans = "";
		HashMap<String, String> names = em.getOnAndOfflineNicksForName(username);
		if (names.size() == 1)
			ans = names.get(username);
		else if (names.size() < 1)
			ans = null;
			
		return ans;
	}
	
	/**
	 * Returns a HashMap of Minecraft usernames and nicknames based on partial matches
	 * for usernames. The HashMap maps username -> nickname and will contain all
	 * matches found for the partial username from players who have played on the
	 * current server.
	 * <br>
	 * If a player has not been assigned a nickname on the server, their "nickname" will be the same as their Minecraft username
	 * <br>
	 * If there are no matches, an empty HashMap is returned.
	 * <br>
	 * This function is disk intensive. It's recommended to make an asynchronous call to it
	 * 
	 * @param partial	A partial or full Minecraft username to search for associated nicknames
	 * @return			A HashMap mapping Minecraft usernames to nicknames containing username matches for <b>partial</b>
	 * @see				NRAPI#getNick(String) getNick(String)
	 */
	public HashMap<String, String> getNicks(String partial)
	{
		return em.getOnAndOfflineNicksForName(partial);
	}
	
	/**
	 * Returns a HashMap of Minecraft usernames and nicknames based on partial matches
	 * for nicknames. The HashMap maps username -> nickname and will contain all
	 * matches found for the partial nickname from players that have nicknames on the
	 * current server.
	 * <br>
	 * If there are no matches, an empty HashMap will be returned.
	 * <br>
	 * This function is disk intensive. It's recommended to make an asynchronous call to it
	 * 
	 * @param partial	A partial or full nickname. Should not include a tilde or any other "Hi I'm a nickname" indicators
	 * @return			A HashMap mapping Minecraft usernames to nicknames containing nickname matches for <b>partial</b>
	 */
	public HashMap<String, String> getNames(String partial)
	{
		return em.getOnAndOfflineNamesForNick(partial);
	}

}
