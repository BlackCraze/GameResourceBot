Setting Up the GameResourceBot - short version
==============================================
v1.2, by PellaAndroid
=====================


*****ATTENTION*****
*****ATTENTION*****
*****ATTENTION*****

This document is intended to be used as a quick reference only, usually by someone who's done this setup before.
This document is a simple list of the steps required to set up the GameResourceBot, and nothing more.

The long version of this document gives complete explanations for every step in the process.
If you are setting up this app for the first time, we strongly recommend using the long version of this document.

*****ATTENTION*****
*****ATTENTION*****
*****ATTENTION*****


BEFORE YOU BEGIN, please understand what this setup requires.
This setup requires approximately one hour of your time, give or take 15 minutes.

Setting up and running the GRB requires that you have accounts on these websites:
*	GitHub
*	Heroku
*	Discord, in the developer area

In addition to the accounts listed above, you must have the Manage Server permission on your Discord server.

1. Launch your web browser.
  - Tab #1 - Log in to GitHub.	https://www.github.com
  - Tab #2 - Log in to Heroku.	https://www.heroku.com
  - Tab #3 - Log in to the developer area for Discord. 	https://discordapp.com/developers/applications/me
 
2. Create a channel for the bot on your Discord server.

1. Discord (Tab #3) 
* Create a new app
* Create a bot user.
	* Required: APP NAME
	* Optional: APP DESCRIPTION
		* Recommended text: Developed by Discord user BlackCraze#4294
	* Optional: APP ICON
	* Ignore: everything else

1. Invite the bot https://discordapp.com/oauth2/authorize?client_id=**[ClientID]**&scope=bot&permissions=66186303 on a new tab.

1. GitHub (Tab #1) 
- [ ] Fork the code to a new repository on your account.	https://github.com/BlackCraze/GameResourceBot

1. Heroku (Tab #2) 
- [ ] Deploy the bot app.	https://heroku.com/deploy?template=https://github.com/BlackCraze/GameResourceBot
- [ ] Variables in 
  - [ ] *italic* can only be set on Heroku-Interface.
  - [ ] **bold** is default setting if not used.

Heroku Variable | Values | Discord Setting | 
------------ | ------------- | -------------
*GRB_DISCORD_TOKEN* | ? | Paste your token (from Tab #3) into the box provided.
*CHANNEL* | ? | Enter the name of your bot's channel on your Discord server, WITHOUT THE #
LANGUAGE | de | Deutsch (German)
|| en | English
|| pt | Português (Portuguese)
OCR_RESULT | **on** | write text in the channel to easily troubleshoot
|| off | will not write feedback of ocr results into the channel
DELETE_PICTURE_MESSAGE | **off** | remove picture from channel after processing it.
|| on | will not delete these messages

- [ ] Connect to GitHub and Deploy.

6. Heroku
- [ ] Configure for automatic updates
- [ ] Start the bot. Configure dynos
- [ ] Enable Automatic Deploys
- [ ] "Resources" tab > Turn the switch on

7. Heroku 
- [ ] Verify your account on https://devcenter.heroku.com/articles/account-verification

1. GitHub 
* Configure computer for source code updates.
  * GRB_update_MSWin
  * GRB_update_MacOS
  * GRB_update_Linux
