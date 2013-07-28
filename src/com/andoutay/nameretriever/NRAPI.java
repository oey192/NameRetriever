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
	 * If the given player has no nickname, it returns exactly what was passed in via the
	 * username parameter. If no user with the given username is found, it returns null.
	 * 
	 * @param 	username	The full username of the player you would like to find a nickname for
	 * @return				the nickname associated with the given Minecraft username
	 * @see					NRAPI#getNicks(String) getNicks
	 */
	public String getNick(String username)
	{
		//return em.getNickname(username);
		return null;
	}
	
	/**
	 * Returns a HashMap of Minecraft usernames and nicknames based on partial matches
	 * for usernames. The HashMap maps username -> nickname and will contain all
	 * matches found for the partial username from players who have played on the
	 * current server. If there are no matches, an empty HashMap is returned.
	 * For players who have no nickname, it will contain an entry like
	 * whosyourdaddy -> whosyourdaddy
	 * 
	 * @param partial	A partial or full Minecraft username to search for associated nicknames
	 * @return			A HashMap mapping Minecraft usernames to nicknames containing username matches for partial
	 */
	public HashMap<String, String> getNicks(String partial)
	{
		//return em.getNicknames(partial);
		return null;
	}
	
	/**
	 * Returns a HashMap of Minecraft usernames and nicknames based on partial matches
	 * for nicknames. The HashMap maps username -> nickname and will contain all
	 * matches found for the partial nickname from players that have nicknames on the
	 * current server. If there are no matches, an empty HashMap will be returned.
	 * 
	 * @param partial	A partial or full nickname. Should not include a tilde or any other "Hi I'm a nickname" indicators
	 * @return			A HashMap mapping Minecraft usernames to nicknames containing nickname matches for partial
	 * @see				NRAPI#getAllNames(String) getAllNames
	 */
	public HashMap<String, String> getNames(String partial)
	{
		//return em.getNames(partial);
		return null;
	}
	
	/**
	 * Returns a HashMap of Minecraft usernames and nicknames based on partial matches for nicknames.
	 * The HashMap maps username -> nickname and will contain all matches found for the partial
	 * nickname from players how have played on the current server.
	 * <p>
	 * The HashMap will contain any matches for display names whether the player has a nickname or not
	 * <p>
	 * If there are no matches, an empty HashMap will be returned.
	 * 
	 * @param partial	A partial or full nickname. Should not include a tilde or any other "Hi I'm a nickname" indicators
	 * @return			A HashMap mapping Minecraft usernames to nicknames containing nickname matches for partial
	 * @see				NRAPI#getNames(String) getNames
	 */
	public HashMap<String, String> getAllNames(String partial)
	{
		//return em.getAllNames(partial);
		return null;
	}

}
