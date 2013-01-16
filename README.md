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


Configuration
===

showStatus:<br/>
When true, the online or offline status of a plyaer is displayed along with their real- and nick-names

getOfflineAlways:<br/>
When true, the plugin searches through offline players even when it finds online players that match the parameter in the /realname and /realnick commands

cmdRegex1:<br/>
the regular expression for matching all commands that should have their first argument replaced by the "realname" of that argument

cmdRegex2:<br/>
the regular expression for matcching all commands that should have their frist two arguments replaced by the "realnames" of theose arguments
