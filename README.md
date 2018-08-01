# GameResourceBot
a Discord bot for managing guild resources

### CONTENTS
1. Quick reference
2. Setting up the Game Resource Bot
    1. Short version (for those experienced with setting up GRB)
    2. Long version (for first-timers)
3. Updating the Game Resource Bot
    1. on Microsoft Windows
    2. on MacOS
    3. on Linux
4. Changelogs
    1. GRB Setup Changelog
    2. GRB Update Changelog



## Quick reference
    [content to be added later]

## Setting up the Game Resource Bot
#### Setting Up the GameResourceBot - short version
#####  v1.2, by PellaAndroid
```
*****ATTENTION*****
*****ATTENTION*****
*****ATTENTION*****

This guide is intended to be used as a quick reference only, usually by someone who's done this setup before.
This guide is a simple list of the steps required to set up the GameResourceBot, and nothing more.

The long version of this guide gives complete explanations for every step in the process.
If you are setting up this app for the first time, we strongly recommend using the long version of this guide.

*****ATTENTION*****
*****ATTENTION*****
*****ATTENTION*****


BEFORE YOU BEGIN, please understand what this setup requires.
This setup requires approximately one hour of your time, give or take 15 minutes.

Setting up and running the GRB requires that you have accounts on these websites:
    GitHub
    Heroku
    Discord, in the developer area

In addition to the accounts listed above, you must have the Manage Server permission on your Discord server.

==========

Step 0: Launch your web browser.
Step 0(a): Tab #1 - Log in to GitHub.    https://www.github.com
Step 0(b): Tab #2 - Log in to Heroku.    https://www.heroku.com
Step 0(c): Tab #3 - Log in to the developer area for Discord.     https://discordapp.com/developers/applications/me
Step 0(d): Create a channel for the bot on your Discord server.

==========

Step 1: Discord (Tab #3) - Create a new app. Create a bot user.
    Required: APP NAME
    Optional: APP DESCRIPTION
        Recommended text: Developed by Discord user BlackCraze#4294
    Optional: APP ICON
    Ignore: everything else
    Tab #4 - Invite the bot:    https://discordapp.com/oauth2/authorize?client_id=[ClientID]&scope=bot&permissions=66186303


Step 2: GitHub (Tab #1) - Fork the code to a new repository on your account.    https://github.com/BlackCraze/GameResourceBot


Step 3: Heroku (Tab #2) - Deploy the bot app.    https://heroku.com/deploy?template=https://github.com/BlackCraze/GameResourceBot
    Variables marked + can be changed on Discord later.
        GRB_DISCORD_TOKEN - Paste your token (from Tab #3) into the box provided.
        CHANNEL - Enter the name of your bot's channel on your Discord server, WITHOUT THE #
        + LANGUAGE
            de - Deutsch (German)
            en - English
            pt - Português (Portuguese)
            More languages coming soon!
        + OCR_RESULT - default setting: "on"
        + DELETE_PICTURE_MESSAGE - default setting: "off"
        Ignore: everything else.
    Connect to GitHub. Deploy.


Step 4: Heroku - Configure for automatic updates. Start the bot. Configure dynos.
    Enable Automatic Deploys.
    "Resources" tab - Turn the switch on.


Step 5: Heroku - Verify your account.    https://devcenter.heroku.com/articles/account-verification


Step 6: GitHub - Configure computer for source code updates.
    See section 3, "Updating the GameResourceBot".
```

#### Setting Up the GameResourceBot - long version
##### v1.2, by PellaAndroid

```
*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****
*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****
*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****

    During this process, you WILL encounter technical terms, both within this guide and without.
    If you are not a "techie", these terms will almost certainly be unfamiliar to you.
    Even if you are a "techie", these terms probably will be unfamiliar to you, unless you are a developer.
    Even if you are a developer, these terms may be unfamiliar to you, unless you have experience with Java and the required websites.

    That's the bad news. Here's the good.
        This guide is here to help you. Do not be afraid.
        If you see an unfamiliar term in this guide, you can expect the next thing you read to explain that term.
        This guide is here to help you. Discard your fear.
        If you see an unfamiliar term on a website, you can expect the relevant section of this guide to explain that term.
        This guide is here to help you. Proceed with confidence.

    If you already know all the technical terms you're going to encounter, then obviously you can skip the explanations mentioned above.
    We're not going to tell you exactly which parts you can skip. You're the smart one here, so put on your big girl panties and figure it out yourself.

    If you feel scared at this point, that's bad. Stop reading now.
    If you feel nervous at this point, that's excellent. It means you're paying attention and don't want to screw up anything.
    If you feel bold at this point, that's good. Remember to set aside your assumptions and to follow these instructions exactly.

*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****
*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****
*****SUPER-SPECIAL, SUPER-IMPORTANT NOTE*****


==========


BEFORE YOU BEGIN, please understand what this setup requires.
Firstly, this setup requires approximately one hour of your time, give or take 15 minutes.
    Ensure you have sufficient time to devote to this process.
    Rushing this process is an excellent way to screw it up, forcing you to start over.
    Ensure you have sufficient time to devote to this process.

Setting up and running the GRB requires that you have accounts on these websites:
    GitHub - This is where the source code lives. (The "source code" contains all the computer instructions that tell the bot what to do and how to do it.)
    Heroku - This site will host the bot application. (Each application needs a "host" that actually executes the source code.)
    Discord, in the developer area - This is how you will get the bot to your guild.

In addition to the accounts listed above, you must have the Manage Server permission on your Discord server.
If you do not have the Manage Server permission on your Discord server, you have the following options:
    1) Request the Manage Server permission on your Discord server. Have your request granted.
    2) Find someone else in your guild who has the Manage Server permission. Convince them to go through this process.
    3) Forget about using GameResourceBot.

    If you chose option #3, stop reading now.
    If you chose option #2, stop reading and escort the individual you selected to this guide.

If you do not have these accounts already, you may create them during the setup process.
If you do not have these accounts and do not wish to create these accounts, you have the following options:
    1) Change your mind. Create the accounts.
    2) Find someone else in your guild who has the required accounts or is willing to create them. Convince them to go through this process.
    3) Forget about using GameResourceBot.

    If you chose option #3, stop reading now.
    If you chose option #2, stop reading and escort the individual you selected to this guide.

If you're still reading, you want to make this happen. That, or someone else chose option #2 somewhere above, and they picked you. Congratulations. Either way, let's begin.


==========


Step 0: Launch your web browser. We will OPEN several tabs during this process. Closing them is not our style.
Step 0(a): Tab #1 - Log in to GitHub.    https://www.github.com
    If you do not already have an account on GitHub, create it now. Then leave the tab open.
    No step-by-step instructions here. We assume you're bright enough to figure out a signup process for a website.

Step 0(b): Tab #2 - Log in to Heroku.    https://www.heroku.com
    If you do not already have an account on Heroku, create it now. Then leave the tab open.
    Again, no step-by-step instructions, especially since we know you've done this before.

Step 0(c): Tab #3 - Log in to the developer area for Discord.     https://discordapp.com/developers/applications/me
    Since you're setting up a bot to run on a Discord server, we will assume you already have an account on Discord.
    You will use your existing Discord account, so there is no need to create one.
    However, you probably aren't familiar with Discord's developer area. Go there now, and set up "My Apps".
    No, there are no step-by-step instructions here, either. You got this.

Step 0(d): Create a channel for the bot on your Discord server.
    Your new bot will need a home, a place where you can upload screenshots, type commands, and view the bot's output.
    Use your normal method to access your guild's server on Discord. (web, desktop app, mobile app, whatever)
    Create a new text channel. Name the channel whatever you like. (The most common name people use is #bot. Go figure.)


==========


Step 1: Discord - Create a new app.
    Obviously, Discord is where we will interact with the bot and see all the cool things it can do.
    Therefore, let's get things started by creating an app on Discord. We also need to create a bot user for the app, for obvious reasons.
    In a later step, we'll tell Heroku how to talk with this app. In turn, Discord will use this app to direct your bot to your server.

    Tab #3
        Great big button on the right side: New App
        Required: APP NAME
        Optional: APP DESCRIPTION
            In addition to describing the bot and its function, this is an excellent place to give credit to the bot's developer.
            Recommended text: Developed by Discord user BlackCraze#4294
        Optional: APP ICON
        Ignore: everything else
        Blue button at the bottom: Create App

    Now you should see: GREAT SUCCESS!
        If not, use whatever information the website gives you to solve the error.

    Scroll down to section: Bot
        Blue button on the right: Create a Bot User
        Read the warning!
        Blue button: Yes, do it

    We have an app. The app has a bot. The bot wants to move in to its new home, but it doesn't know where home is.
    We must create a special link that will act like GPS for the bot.
        First, copy this link:    https://discordapp.com/oauth2/authorize?client_id=[ClientID]&scope=bot&permissions=66186303
        Tab #4 - This one is new. Open it now.
            Paste the link into Tab #4's address bar. DO NOT PRESS <ENTER>!!
            Our link isn't quite ready to do its job yet. Look closely at it. Roughly in the centre of it is this text: [ClientID]
            We need to replace that text--including the square brackets--with the Client ID of our new Discord app.
        Tab #3
            On the right side, scroll to the top.
            Find the Client ID. It's an 18-digit number right at the top. You can't miss it.
            Copy all the digits.
        Tab #4
            Delete [ClientID] from the link, and paste your number in the same spot.
            NOW press <Enter>.
            When the page loads, select the correct Discord server from the drop-down list. Then authorise the bot.
            We no longer need Tab #4. Shut it down, or let it ride. Your call.

    Go to your guild's Discord server.
        Hooray! The bot found its new home and has already moved in.
        However, we can't interact with it yet. After moving and unpacking, the bot needs a nap.
        If you added an APP ICON earlier, you will not see it yet. The bot will hang its pictures when it wakes up.
        Unfortunately, the moving van did not bring the bot's brain. We will build its brain in Step 2.


==========


Step 2: GitHub - Fork the code to a new repository on your account.    https://github.com/BlackCraze/GameResourceBot
    Our sleeping bot needs a brain, a set of instructions for its bot duties. Those instructions are in the source code.
    BlackCraze keeps the master source code on his account. For your bot, you will need your own copy of the source code.
    To accomplish this, you must duplicate the master source code to your account.
        Your copy of the source code is called a "repository".
        The process of duplicating the source code is called "forking". Each existing copy of the source code (other than the master copy that BlackCraze keeps) is called a "fork".
        A repository can contain original code (BlackCraze) or it can be a fork (you, and others before you).

    Tab #1 - Same site, different page. Copy the link at the beginning of Step 2. Paste it into Tab #1 and go!
        In the top left corner of the browser window, verify this text is present: BlackCraze / GameResourceBot
        In the top right corner of the browser window, click the word "Fork" with the funny symbol next to it.
        Watch the magic happen.

    We have a bot. We have a brain. We do not have a battery pack to power the brain--yet.
    We're done here. Moving on.


==========


Step 3: Heroku - Deploy the bot app.    https://heroku.com/deploy?template=https://github.com/BlackCraze/GameResourceBot
    Welcome to the host site, our battery pack.
    Every application on the Web requires a host. The host has several functions.
        The source code is in plain text that humans can read. Computers can read it, too, but not very well. Computers read plain text extremely slowly, almost like a kindergartener learning to read.
        The host converts the plain text into the 1s and 0s that computers prefer. This conversion process is called "compiling".
        Compiling the source code is like welding the battery pack into the brain. The host puts the different parts together into a single unit that computers can read extremely quickly. This single unit **IS** the app, which is our bot.
        After it compiles the source code, the host runs the bot app. But we don't want to interact with the bot here. We want to go to the bot's new home, wake it up, and have a big housewarming party.
        Therefore, the host (Heroku) and the bot (at home, on Discord) send data back and forth to each other through the Internet.
        That is, they WILL. They can't yet, because they haven't met yet. Our task here is to introduce them to each other.

    Tab #2 - Same site, different page. Copy the link at the beginning of Step 3. Paste it into Tab #2 and go!
        Create a name for the bot. Yes, again. This name is unique to Heroku. The bot will not use this name.
        Heroku will use this name to keep track of our bot in its filing cabinet. For this reason, the name must be unique. Be creative.
        Select a server region (USA or Europe).

    Tab #3
        Scroll down to the section "Bot".
        Next to "Token" is a link that reads, "Click to reveal". Click it. It will reveal something.
        Now your token is revealed. This long string of random characters is your bot's GPS address on Discord.
        Highlight all the characters in the token and copy it.

    Tab #2
        Scroll down to the section "Config Variables".
        The bot will configure itself using these variables when it wakes up. Variables marked + can be changed on Discord later.
            GRB_DISCORD_TOKEN - Paste your token into the box provided.
            CHANNEL - Enter the name of your bot's channel on your Discord server, WITHOUT THE #
            + LANGUAGE - This will be your bot's default language.
                de - Deutsch (German)
                en - English
                pt - Português (Portuguese)
                More languages coming soon!
            + OCR_RESULT - After it processes a screenshot, the bot will list the results so you can verify they are correct. If you change this to "off", it won't.
            + DELETE_PICTURE_MESSAGE - If you change this to "on", the bot will delete a screenshot after it processes it.
            Ignore: everything else.
        Purple button: Deploy app

    On the next screen, we will tell Heroku where to find the source code, so it can compile it.
        Find the second section: "Deployment method". Click the GitHub button.
        Ideally, Heroku will sense that you are logged in to GitHub and find you automatically. If it does not, it will ask for your GitHub username and password.
        Purple button: Connect to GitHub - This allows Heroku to grab the source code from your repository.
        Then Heroku will display a box labelled: Search for a repository to connect to. Inside this box is your GitHub username.
        Purple button on the right: Search
        Then Heroku will display the full name of your GitHub repository.
        White button on the right: Connect
        At the bottom, find the section "Deploy a GitHub branch". Select "master".
        Grey button on the right: Deploy Branch
        Watch the magic happen.

    You are doing GREAT work! Relax for a moment. Take a deep breath or two. The most difficult part is done.
    We're almost there. Only some minor configuration left to go.


==========


Step 4: Heroku - Configure for automatic updates. Start the bot. Configure dynos.
    We created a home for the bot on Discord.
    On GitHub, we made our own copy of the bot's source code.
    Heroku compiled the source code and deployed the bot app--brain, battery pack, and all.
    The bot and Heroku have made formal introductions and are ready to begin sending data back and forth. All they need is for you to say, "Go!"

    And you will, very soon. But first, we have a bit of unfinished business to complete.
        Occasionally, BlackCraze updates the master source code with new features, bug fixes, and other things.
        When that happens, you will need to update your GitHub repository with the new source code. [See section 3, "Updating the GameResourceBot".]
        When that happens, Heroku must retrieve the new source code, compile it, and redeploy the app.
        Here's the cool part: Heroku can do all of that automatically, without any human interaction.

    Tab #2
        To make the coolness happen, move your eyes slightly up the screen to the section "Automatic deploys".
        "Enable automatic deploys from GitHub" - Select "master".
        Grey button in the middle: Enable Automatic Deploys
        The unfinished business is now finished business.

    And now, we start the bot!
    Scroll to the top of the browser window. Click the tab: "Resources".

    The first section is "Free Dynos".
        Dynos are like small plastic containers in a refrigerator.
        All the different apps on Heroku are like food in the containers.
        The dynos protect the apps from external things that can cause problems, and from each other.
        Dynos come in two main categories: Free and Not Free. GameResourceBot LOVES the Free ones!

    On the far right side of "Free Dynos", from right to left, are: pencil icon, zero money, on/off switch.
        The on/off switch is off. We need to switch it on, but we can't because it's greyed out.
        Click the pencil icon to un-grey the on/off switch.
        Click the switch to turn it on.
        Purple button: Confirm

********** ********** ********** ********** ********** ********** ********** ********** **********

    YOU DID IT!! Your bot is now awake and waiting for you to tour its new home.
        Use your normal method to access your guild's server on Discord.
        Verify that the bot is online. If you added an APP ICON earlier, it is visible now.

********** ********** ********** ********** ********** ********** ********** ********** **********

    The setup of GameResourceBot is complete.
    Go get a beer, a glass of wine, some apple juice, or whatever tasty beverage you prefer.
    Take a break. You earned it.
    After your break, come back here and continue reading. The information below is brief, useful, and important.


==========


Step 5: Heroku - Verify your account to spend more time with your new bot.
    Accounts that use free dynos are allowed up to 550 hours of free dynos each month.
    That amount of time is enough to run the bot during Deep Town's weekend events.
    That amount of time is NOT enough to run the bot 24/7.

    IF YOU WISH, you may verify your Heroku account by placing a credit card or debit card on file with them.
        Your card will never be charged unless you add something that costs money. YOU WILL KNOW if this happens.
        Verified accounts that use free dynos are allowed up to 1,000 hours of free dynos each month.
        That amount of time is enough to run the bot 24/7, with more than 25% left over.

    The best place to get more information about this is directly from Heroku.
        https://devcenter.heroku.com/articles/account-verification
    

==========


Step 6: GitHub - Configure computer for source code updates.
    This section's title is misleading.
    This section contains no instructions at all.

    This section is a reminder.
    Step 4 mentioned a separate guide in section 3, "Updating the GameResourceBot". The instructions you need are in that guide.
    "Updating the GameResourceBot" is available in three different flavours, for different operating systems.
        MSWin
        MacOS
        Linux

    This section also is a warning.
    The NOT FIRST time you need to synchronise your forked repository with BlackCraze's master repository, you can accomplish the task in two minutes or less.

    OK, to be honest, the FIRST time you do this, you still can accomplish the task in two minutes or less.
    The bad news: Before you reach the Promised Land of Two-Minute Synchronisation, you must tread through another setup process.
    The good news: That setup process is much shorter than this one.
```

## Updating the GameResourceBot
#### Updating the GameResourceBot (MS Windows)
##### v1.0, by PellaAndroid
```
*****ATTENTION*****

    If you previously completed all steps in this guide and need to update again, skip to STEP 4.

*****ATTENTION*****


The GRB setup process you already encountered is the same for everyone. The GRB update process here is not.
Which GRB update process you will use depends on the operating system your computer uses.
If your computer's operating system is Microsoft Windows 7 or later (64-bit), this guide is for you.


==========


This guide assumes you are familiar with the guide in section 2, "Setting Up the Game ResourceBot", meaning you have gone through
all the steps in that guide and have a general understanding of all the technical terms in that
guide. Otherwise, why would you even be reading this guide? Therefore, if you encounter something
in this guide that you do not understand and is not explained in this guide, please refer to that guide.

==========


During Step 4 of the GRB Setup, we mentioned briefly that BlackCraze occasionally updates the master source code, which is in his master repository.
During that same step, we configured Heroku to deploy a shiny new version of the bot every time the source code on GitHub changes. But there's a catch.

Your Heroku account is not connected to BlackCraze's GitHub account. It's connected to YOUR GitHub account.
Therefore, the only way for you to get a shiny new bot is to synchronise your forked repository with BlackCraze's master repository.

Unlike Heroku, GitHub does not have a way to make this happen without human interaction.
This is because GitHub is a really busy place. Imagine if someone attempted to set up a picnic in the traffic lanes of a major freeway during rush hour. The result would be bad for the picnickers and for the motorists, and it still would be LESS chaotic than GitHub with an auto-synchronise function. Trust us, you don't want that.


==========


STEP 1: INSTALL GITHUB DESKTOP
    your web browser        https://desktop.github.com/
        Large button in the centre: Download for Windows (64bit)
        Setup app will download.
        Locate the file you just downloaded (GitHubDesktopSetup.exe). Double-click it to start the setup.
        After setup, the GitHub Desktop app will launch automatically.


==========


STEP 2: LOG IN TO GITHUB
    There are two different ways to log in. You need only one (if it is successful).
    
    SUPER-EASY METHOD
        GitHub Desktop
            Menu bar: File > Options...
            Top left: Accounts
            Blue link near the bottom: Sign in using your browser

        your web browser
        ATTENTION: Specifics will vary, depending on the browser you are using.
            Near the bottom: Check "Remember my choice for x-github-client links".
            In the centre of the pop-up window, select GitHubDesktop.exe
            Bottom right: Open link
        
        GitHub Desktop
            Menu bar: File > Options...
            If you see your GitHub username here, you're good. Skip to STEP 3.
            If you don't see your GitHub username here, proceed with EASY METHOD (next).

    EASY METHOD
        GitHub Desktop
            Menu bar: File > Options...
            Top left: Accounts
            Far right, upper blue button: Sign in
            Enter your GitHub login credentials.
            Blue button at bottom: Sign in
            Menu bar: File > Options...
            If you see your GitHub username here, you're good. Skip to STEP 3.
            If you don't see your GitHub username here, something is wrong. Begin STEP 2 again.


==========


STEP 3: CLONE YOUR REPOSITORY
    Because of the unique ways that GitHub works, the only way to synchronise your forked repository with BlackCraze's master repository is to create a duplicate of your repository on your computer's hard drive.
        Yes, it probably seems weird to you. It seems weird to most people, but it's how GitHub works.
        Sometimes, other people help BlackCraze write the source code.
        A helper often keeps a working copy of their repository on their computer's hard drive.
        The helper then transfers their work from their working copy to the master repository.
        You are going to pretend to be a helper. This actually simplifies things for GitHub.
        This allows GitHub to use one update method for helpers and non-helpers, instead of two methods.
    
    your web browser        https://www.github.com
        Scroll to the bottom of the page.
        Bottom right: Section "Your repositories": Click GameResourceBot
        Far right: Green button: Clone or download
        Bottom left of small pop-up window: Open in Desktop
    
    GitHub Desktop (Windows switches to this app automatically.)
        Local path: Default is your user directory, then \Documents\GitHub\GameResourceBot
            Recommended: Keep the default.
            If you prefer a different path, click Choose... and select the folder you prefer.
        Blue button: Clone
        Watch the magic happen.
    
    Congratulations! The hardest part is done. In the next step, we will synchronise your forked repository with the master repository. For future updates, the next step is the only step you will need to do.


==========


STEP 4: SYNCHRONISE YOUR REPOSITORY
    GitHub Desktop
        Below the menu bar, second button should read: Current branch: master
            If it does not, click it and select "master".
        Menu bar: Repository > Pull
            OR Large button below the menu bar, on the right: Fetch origin
        Menu bar: Branch > Merge into current branch...
            Bottom: Other branches: Select upstream/master
            Blue button at bottom: Merge into master
        Menu bar: Repository > Push
            OR Large button below the menu bar, on the right: Push origin
    
    That's it! Heroku will now see the changes and update your bot.
    Enjoy!
````

#### Updating the GameResourceBot (MacOS)
    [content to be added later]

#### Updating the GameResourceBot (Linux)
    [content to be added later]

## Changelogs
#### GRB Setup Changelog
```
v1.2
+ BOTH: Added Step 6, referring to the GRB Update files.

v1.1
+ LONG: Revised text for switching on the bot.
+ LONG: Rearranged text at the beginning.
+ BOTH: Added pt to the list of available languages.
+ other minor corrections
```
#### GRB Update Changelog
    [no changes yet]