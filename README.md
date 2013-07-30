NameRetriever
===

NameRetriever extends the /realname command from Essentials to work for offline players and allows players to get nicknames based off actual names. It also applies the realname command to the first parameter of any command listed in the config

When accessing log files from Essentials, this plugin uses a separate thread to prevent lag in the main thread


Commands
===

/realname - Shows the realname of a player based on their displayname. If no one is found with a matching displayname, it shows the realname and nickname of a player based on their realname

/realnick - shows the displayname of a player based on their realname

/nameretriever reload - reloads the config file

/nameretriever version - shows the current version

/nameretriever help - shows help for the plugin

Note: /nr is an alias for nameretriever


Permissions
===

nameretriever.realname<br/>
Allows use of the /realname command

nameretriever.realnick<br/>
Allows use of the /realnick command

nameretriever.reload<br/>
Allows use of the /nameretriever reload command

nameretriever.version<br/>
Allows use of the /nameretriever version command

nameretriever.help<br/>
Allows use of the /nameretriever help command


Configuration
===

showStatus:<br/>
When true, the online or offline status of a plyaer is displayed along with their real- and nick-names

getOfflineAlways:<br/>
When true, the plugin searches through offline players even when it finds online players that match the parameter in the /realname and /realnick commands

nickIndicatorChar:<br/>
The character to display before someone's nickname. This only effects results returned via the realname and realnick commands. By default it is set to ~ to match Essentials' default

useAsync:<br/>
Set whether the plugin should make asynchronous calls when accessing nicknames stored on disk. It is recommended this be turned on especially on large servers to prevent server lag. The more players who have played on a server, the more time accessing the disk will take

cmdRegex1:<br/>
the regular expression for matching all commands that should have their first argument replaced by the "realname" of that argument

cmdRegex2:<br/>
the regular expression for matcching all commands that should have their frist two arguments replaced by the "realnames" of theose arguments


API
===

NRAPI is the name of the API class. Use the static method NRAPI.getAPI() to get the singleton instance of the API. (It returns an object of type com.andoutay.nameretriever.NRAPI) All following methods are ment to be called on the singleton instance.

<br/>
String getNick(String)

Returns the nickname associated with whatever Minecraft username is passed in. If an incomplete Minecraft username is passed in and more than one Minecraft username matches (based on players who have played on the current server), an empty string is returned. If no Minecraft username matches, null is returned.

<br/>
HashMap<String, String> getNicks(String)

Given a search string, getNicks returns a HashMap containing all matching Minecraft usernames and their associated nicknames including Minecraft usernames that do not have associated nicknames.
<br/>
The HashMap is organized with username mapping to nickname (e.g. i_am_alive => ~Bob). For those who do not have a nickname, it maps username to username (e.g. i_am_alive => i_am_alive)
<br/>
In the event that no Minecraft usernames match the search string, an empty HashMap is returned

<br/>
HashMap<String, String> getNames(String)

Given a search string, getNames returns a HashMap containing all matching nicknames and their associated Minecraft usernames. A player's display name does not count as a nickname if it is the same as their Minecraft username. Thus, if "alive" is passed as the search string and i_am_alive is a player who has played on the server who does not have a nickname, they will not be returned in the HashMap. However, if im_cooking has a nickname of WhosAliveNow, im_cooking => ~WhosAliveNow will be returned as a member of the HashMap.
<br/>
Note that the tilde character will be whatever is specified for the nickIndicatorChar and is not guaranteed to be a single character. It is possible for the server administrator to put whatever they like for this config value.
<br/>
In the event that no nicknames match the serach string, an empty HashMap is returned