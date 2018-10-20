# Setting Up GameResourceBot - short version
######  v1.2, by PellaAndroid

*GameResourceBot has a nickname. It likes to be called "GRB", so we call it that most of the time.*

### CONTENTS
1. [About This Guide](#about-this-guide)
2. [Getting Ready](#getting-ready)
3. [The Setup Process](#the-setup-process)

## About This Guide
- This guide is intended to be used as a quick reference only, usually by someone who's done this setup before.  
- This guide is a simple list of the steps required to set up GameResourceBot, and nothing more.  
- [The long version of this guide](./SETUP-long.md) gives complete explanations for every step in the process.  
- If you are setting up this app for the first time, we strongly recommend using [the long version of this guide](./SETUP-long.md).

## Getting Ready
BEFORE YOU BEGIN, please understand what this setup requires.
- This setup process requires approximately one hour of your time, give or take 15 minutes.
- Setting up and running GRB requires that you have accounts on these websites:
	- GitHub
	- Heroku
	- Discord, in the developer area
- In addition to the accounts listed above, you must have the Manage Server permission on your Discord server.

## The Setup Process
#### 0. Start your web browser.
1. Tab #1 - Log in to [GitHub](https://www.github.com).
2. Tab #2 - Log in to [Heroku](https://www.heroku.com).
3. Tab #3 - Log in to the [developer area for Discord](https://discordapp.com/developers/applications/me).
4. Create a channel for GRB on your Discord server.

#### 1. Discord (Tab #3) - Create a new app.
1. Create the app.
    - Required: APP NAME
    - Optional: APP DESCRIPTION
        - Recommended text: `Developed by Discord user BlackCraze#4294`
    - Optional: APP ICON
2. Create a bot user. Invite the bot:
```
https://discordapp.com/oauth2/authorize?client_id=[ClientID]&scope=bot&permissions=66186303
```

#### 2. GitHub (Tab #1) - [Fork the code to a new repository on your account.](https://github.com/BlackCraze/GameResourceBot)

#### 3. Heroku (Tab #2) - [Deploy the GRB app.](https://heroku.com/deploy?template=https://github.com/BlackCraze/GameResourceBot)
1. Variables marked §§ can be changed on Discord later.
	- CHANNEL - Enter the name of your GRB's channel on your Discord server, WITHOUT THE #
	- §§ DELETE_PICTURE_MESSAGE - default setting: "off"
	- GRB_DISCORD_TOKEN - Paste your token (from Tab #3) into the box provided.
	- §§ LANGUAGE
		- de - Deutsch (German)
		- en - English
		- pt - Português (Portuguese)
		- More languages coming soon!
	- §§ OCR_RESULT - default setting: "on"
	- Ignore: everything else.
2. Connect to GitHub. Deploy.

#### 4. Heroku - Configure for automatic updates. Start GRB. Configure dynos.
1. Enable Automatic Deploys.
2. "Resources" tab - Turn the switch on.

#### 5. Heroku - [Verify your account.](https://devcenter.heroku.com/articles/account-verification)

#### 6. GitHub - Configure computer for source code updates.
See ["Updating GameResourceBot"](./README.md#updating-gameresourcebot).
***OR*** go directly to the set of instructions for your computer's operating system.
- [MS Windows](./UPDATE-MSWin.md)
- [MacOS](./UPDATE-MacOS.md) - ***COMING SOON***
- [Linux](./UPDATE-Linux.md) - ***COMING SOON***

[Back to top](#setting-up-gameresourcebot---short-version)

[Back to main `README` file](./README.md)
