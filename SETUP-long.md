# Setting Up GameResourceBot - long version
######  v1.2, by PellaAndroid

*GameResourceBot has a nickname. It likes to be called "GRB", so we call it that most of the time.*

### CONTENTS
1. [About This Guide](#about-this-guide)
2. [Super-Special, Super-Important Note](#super-special-super-important-note)
3. [Getting Ready](#getting-ready)
4. [The Setup Process](#the-setup-process)  
    (0) [Step 0. Start your web browser.](#step-0-start-your-web-browser-we-will-open-several-tabs-during-this-process-closing-them-is-not-really-our-style)  
    (1) [Step 1. Discord - Create a new app.](#step-1-discord---create-a-new-app)  
    (2) [Step 2. GitHub - Fork the code.](#step-2-github---fork-the-code-to-a-new-repository-on-your-account)  
    (3) [Step 3. Heroku - Deploy the GRB app.](#step-3-heroku---deploy-the-grb-app)  
    (4) [Step 4. Heroku - Configure for automatic updates.](#step-4-heroku---configure-for-automatic-updates-start-grb-configure-dynos)  
    (5) [Step-5. Heroku - Verify your account.](#step-5-heroku---verify-your-account-to-spend-more-time-with-your-new-grb)  
    (6) [Step 6. GitHub - Prepare for updates.](#step-6-github---configure-computer-for-source-code-updates)

## About This Guide
42. Lines--like this one--that start with numbers are actions. You must do these actions to set up GRB successfully.  
    xii. Sometimes, numbers appear as lowercase Roman numerals. These also are actions required for successful setup.  
        a. A couple of ordinary letters slipped in here, too. These are required actions, as well.


- Lines--like this one--starting with bullets (or with nothing at all!) contain additional information. This information could be:
	- critical information about how to complete a numbered line above it; or
	- explaining something above it, to help you understand what you're doing.
- Either way, it's definitely important!!

Finally, all the headers for the primary step numbers in this guide (the final seven lines of [CONTENTS](#contents)) use sentence case--that is, they are capitalised like any ordinary sentence would be. This will become an important clue for you later.

## ***SUPER-SPECIAL, SUPER-IMPORTANT NOTE***

During this process, you WILL encounter technical terms, both within this guide and without.
- If you are not a "techie", these terms will almost certainly be unfamiliar to you.
- Even if you are a "techie", these terms probably will be unfamiliar to you, unless you are a developer.
- Even if you are a developer, these terms may be unfamiliar to you, unless you have experience with Java and the required websites.

That's the bad news. Here's the good.
- This guide is here to help you. Do not be afraid.
- If you see an unfamiliar term in this guide, you can expect the next thing you read to explain that term.
- This guide is here to help you. Discard your fear.
- If you see an unfamiliar term on a website, you can expect the relevant section of this guide to explain that term.
- This guide is here to help you. Proceed with confidence.

If you already know all the technical terms you're going to encounter, then obviously you can skip the explanations mentioned above.  
We're not going to tell you exactly which parts you can skip. You're the smart one here, so put on your big girl panties and figure it out yourself.

- If you feel scared at this point, that's bad. Stop reading now.
- If you feel nervous at this point, that's excellent. It means you're paying attention and don't want to screw up anything.
- If you feel bold at this point, that's good. Remember to set aside your assumptions and to follow these instructions exactly.  

## Getting Ready
BEFORE YOU BEGIN, please understand what this setup requires.
- This setup process requires approximately one hour of your time, give or take 15 minutes.
	- Ensure you have sufficient time to devote to this process.
	- Rushing this process is an excellent way to screw it up, forcing you to start over.
	- Ensure you have sufficient time to devote to this process.
- Setting up and running GRB requires that you have accounts on these websites:
	- GitHub - This is where the source code lives. (The "source code" contains all the computer instructions that tell GRB what to do and how to do it.)
	- Heroku - This site will host the GRB application. (Each application needs a "host" that actually executes the source code.)
	- Discord, in the developer area - This is how you will get GRB to your guild.
- In addition to the accounts listed above, you must have the Manage Server permission on your Discord server.

If you do not have the Manage Server permission on your Discord server, you have the following options:
1. Request the Manage Server permission on your Discord server. Have your request granted.
2. Find someone else in your guild who has the Manage Server permission. Convince them to go through this process.
3. Forget about using GameResourceBot.

If you chose option #3, stop reading now.  
If you chose option #2, stop reading and escort the individual you selected to this guide.

If you do not have these accounts already, you may create them during the setup process.  
If you do not have these accounts and do not wish to create these accounts, you have the following options:
1. Change your mind. Create the accounts.
2. Find someone else in your guild who has the required accounts or is willing to create them. Convince them to go through this process.
3. Forget about using GameResourceBot.

If you chose option #3, stop reading now.  
If you chose option #2, stop reading and escort the individual you selected to this guide.

If you're still reading, you want to make this happen. That, or someone else chose option #2 somewhere above, and they picked you. Congratulations. Either way, let's begin.

## The Setup Process
*NOTE: We begin with Step 0 because it's not really part of the setup process. It's actually one more piece of "getting ready" stuff, but it works better here than in the section above. Trust us; you'll see.*

#### Step 0. Start your web browser. We will OPEN several tabs during this process. Closing them is not really our style.
1. Tab #1 - Log in to [GitHub](https://www.github.com).
	1. If you do not already have an account on GitHub, create it now. Then leave the tab open.  
No step-by-step instructions here. We assume you're bright enough to figure out a signup process for a website.
2. Tab #2 - Log in to [Heroku](https://www.heroku.com).
	1. If you do not already have an account on Heroku, create it now. Then leave the tab open.  
Again, no step-by-step instructions, especially since we know you've done this before.
3. Tab #3 - Log in to the [developer area for Discord](https://discordapp.com/developers/applications/me).  
Since you're setting up GRB to run on a Discord server, we will assume you already have an account on Discord.  
You will use your existing Discord account, so there is no need to create one.  
However, you probably aren't familiar with Discord's developer area.
	1. Go there now, and set up "My Apps".  
No, there are no step-by-step instructions here, either. You got this.
4. Create a channel for GRB on your Discord server.
GRB will need a home, a place where you can upload screenshots, type commands, and view GRB's output.
	1. Use your normal method to access your guild's server on Discord. (web, desktop app, mobile app, whatever)
	2. Create a new text channel. Name the channel whatever you like. (The most common name people use is `#bot`. Go figure.)

#### Step 1. Discord - Create a new app.  
Obviously, Discord is where we will interact with GRB and see all the cool things it can do.  
Therefore, let's get things started by creating an app on Discord. We also need to create a bot user for the app, for obvious reasons.  
In a later step, we'll tell Heroku how to talk with this app. In turn, Discord will use this app to direct your GRB to your server.
1. Tab #3
    1. Great big button on the right side: New App
        - Required: APP NAME
        - Optional: APP DESCRIPTION
            - In addition to describing GRB and its function, this is an excellent place to give credit to GRB's developer.
            - Recommended text: `Developed by Discord user BlackCraze#4294`
        - Optional: APP ICON
        - Ignore: everything else
    2. Blue button at the bottom: Create App
2. Now you should see: GREAT SUCCESS!
    1. If not, use whatever information the website gives you to solve the error.
3. Scroll down to section: Bot
    1. Blue button on the right: Create a Bot User
    2. Read the warning!
    3. Blue button: Yes, do it

We have an app. The app has a bot. GRB wants to move in to its new home, but it doesn't know where home is.  
We must create a special link that will act like GPS for GRB.

4. To begin making our GPS, copy this link:  
```
https://discordapp.com/oauth2/authorize?client_id=[ClientID]&scope=bot&permissions=66186303
```
5. Tab #4 - This one is new. Open it now.
 	1. Paste the link into Tab #4's address bar. DO NOT PRESS \[ENTER\]!!
 	2. Our link isn't quite ready to do its job yet. Look closely at it. Roughly in the centre of it is this text: \[ClientID\]  
We need to replace that text--including the square brackets--with the Client ID of our new Discord app.
6. Tab #3
 	1. On the right side, scroll to the top.
 	2. Find the Client ID. It's an 18-digit number right at the top. You can't miss it.
 	3. Copy all the digits.
7. Tab #4
 	1. Delete "\[ClientID\]" from the link, and paste your number in the same spot.
 	2. NOW press \[Enter\].
 	3. When the page loads, select the correct Discord server from the drop-down list. Then authorise GRB.
 	4. We no longer need Tab #4. Shut it down, or let it ride. Your call.
8. Go to your guild's Discord server.
	- Hooray! GRB found its new home and has already moved in.
	- However, we can't interact with it yet. After moving and unpacking, GRB needs a nap.
	- If you added an APP ICON earlier, you will not see it yet. GRB will hang its pictures when it wakes up.
	- Unfortunately, the moving van did not bring GRB's brain. We will build its brain in Step 2.

#### Step 2. GitHub - Fork the code to a new repository on your account.
	https://github.com/BlackCraze/GameResourceBot

Our sleeping GRB needs a brain, a set of instructions for its duties. Those instructions are in the source code.  
BlackCraze keeps the master source code on his account. For your GRB, you will need your own copy of the source code.  
To accomplish this, you must duplicate the master source code to your account.
- Your copy of the source code is called a "repository".
- The process of duplicating the source code is called "forking". Each existing copy of the source code (other than the master copy that BlackCraze keeps) is called a "fork".
- A repository can contain original code (BlackCraze) or it can be a fork (you, and others before you).


1. Tab #1 - Same site, different page. Copy the link at the beginning of Step 2. Paste it into Tab #1 and go!
	1. In the top left corner of the browser window, verify this text is present: `BlackCraze / GameResourceBot`
	2. In the top right corner of the browser window, click the word "Fork" with the funny symbol next to it.
	3. Watch the magic happen.

We have a GRB. We have a brain. We do not have a battery pack to power the brain--yet.
We're done here. Moving on.

#### Step 3: Heroku - Deploy the GRB app.
	https://heroku.com/deploy?template=https://github.com/BlackCraze/GameResourceBot

Welcome to the host site, our battery pack.  
Every application on the Web requires a host. The host has several functions.
- The source code is in plain text that humans can read. Computers can read it, too, but not very well. Computers read plain text extremely slowly, almost like a kindergartener learning to read.
- The host converts the plain text into the 1s and 0s that computers prefer. This conversion process is called "compiling".
- Compiling the source code is like welding the battery pack into the brain. The host puts the different parts together into a single unit that computers can read extremely quickly. This single unit **IS** the app, which is our GRB.
- After it compiles the source code, the host runs the GRB app. But we don't want to interact with GRB here. We want to go to GRB's new home, wake it up, and have a big housewarming party.
- Therefore, the host (Heroku) and GRB (at home, on Discord) send data back and forth to each other through the Internet.
- That is, they WILL. They can't yet, because they haven't met yet. Our task here is to introduce them to each other.


1. Tab #2 - Same site, different page. Copy the link at the beginning of Step 3. Paste it into Tab #2 and go!
	1. Create a name for GRB. Yes, again. This name is unique to Heroku. GRB will not use this name.
	2. Heroku will use this name to keep track of our GRB in Heroku's filing cabinet. For this reason, the name must be unique. Be creative. We cannot stress this enough. BE CREATIVE.
	3. Select a server region (USA or Europe).
2. Tab #3
	1. Scroll down to the section "Bot".
	2. Next to "Token" is a link that reads, "Click to reveal". Click it. It will reveal something.
	3. Now your token is revealed. This long string of random characters is your GRB's GPS address on Discord.
	4. Highlight all the characters in the token and copy it.
###### (This line does not exist. You are not reading this. Please move on.)
3. Tab #2
	1. Scroll down to the section "Config Vars".
	2. GRB will configure itself using these variables when it wakes up. Variables marked §§ can be changed on Discord later. (BUT not without consequences, which [this footnote](#footnote-for-step-3) discusses.)
		- CHANNEL - Enter the name of your GRB's channel on your Discord server, WITHOUT THE #
		- §§ DELETE_PICTURE_MESSAGE - If you change this to "on", GRB will delete a screenshot from the CHANNEL after it processes it. Otherwise, the screenshots stay put.
		- GRB_DISCORD_TOKEN - Paste your token into the box provided.
		- §§ LANGUAGE - This will be your GRB's default language.
			1. If some members of your guild have a primary language different from other members of your guild, select the language most of them use.
			2. The others can ~~deal with it~~ ~~learn everyone else's language~~ change their own language settings later.
				- de - Deutsch (German)
				- en - English
				- pt - Português (Portuguese)
				- More languages coming soon!
		- §§ OCR_RESULT - After it processes a screenshot, GRB will list the results so you can verify they are correct. If you change this to "off", it won't.
		- Ignore: everything else.
	3. Purple button: Deploy app

On the next screen, we will tell Heroku where to find the source code, so it can compile it.

4. Find the second section: "Deployment method". Click the GitHub button.
5. Ideally, Heroku will sense that you are logged in to GitHub and find you automatically. If it does not, it will ask for your GitHub username and password.
6. Purple button: Connect to GitHub
	- This allows Heroku to grab the source code from your repository.
	- Then Heroku will display a box labelled: Search for a repository to connect to. Inside this box is your GitHub username.
7. Purple button on the right: Search  
Then Heroku will display the full name of your GitHub repository.
8. White button on the right: Connect
9. At the bottom, find the section "Deploy a GitHub branch". Select "master".
10. Grey button on the right: Deploy Branch
11. Watch the magic happen.

You are doing GREAT work! Relax for a moment. Take a deep breath or two. The most difficult part is done.  
We're almost there. Only some minor configuration left to go.

#### Step 4: Heroku - Configure for automatic updates. Start GRB. Configure dynos.
We created a home for GRB on Discord.  
On GitHub, we made our own copy of GRB's source code.  
Heroku compiled the source code and deployed the GRB app--brain, battery pack, and all.  
GRB and Heroku have made formal introductions and are ready to begin sending data back and forth. All they need is for you to say, "Go!"

And you will, very soon. But first, we have a bit of unfinished business to complete.
- Occasionally, BlackCraze updates the master source code with new features, bug fixes, and other things.
- After that happens, you will need to update your GitHub repository with the new source code.
- After THAT happens, Heroku must retrieve the new source code, compile it, and redeploy the GRB app.
- Here's the cool part: Heroku can do all of that automatically, without any human interaction.


1. Tab #2
	1. To make the coolness happen, move your eyes slightly up the screen to the section "Automatic deploys".
	2. "Enable automatic deploys from GitHub" - Select "master".
	3. Grey button in the middle: Enable Automatic Deploys

The unfinished business is now finished business.

And now, we start GRB!

2. Scroll to the top of the browser window. Click the tab: "Resources".
	- The first section is "Free Dynos".
		- Dynos are like small plastic containers in a refrigerator.
		- All the different apps on Heroku are like food in the containers.
		- The dynos protect the apps from external things that can cause problems, and from each other.
		- Dynos come in two main categories: Free and Not Free. GRB LOVES the Free ones!
    - On the far right side of "Free Dynos", from right to left, are: pencil icon, zero money, on/off switch.  
    - The on/off switch is off. We need to switch it on, but we can't because it's greyed out.


3. Click the pencil icon to un-grey the on/off switch.
4. Click the switch to turn it on.
5. Purple button: Confirm

**\*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\***

YOU DID IT!! Your GRB is now awake and waiting for you to tour its new home.

6. Use your normal method to access your guild's server on Discord.
7. Verify that GRB is online. If you added an APP ICON earlier, it should be visible now.

**\*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\* \*\*\*\*\*\*\*\*\*\***

The setup of GameResourceBot is complete.  
Go get a beer, a glass of wine, some apple juice, or whatever tasty beverage you prefer.  
Take a break. You earned it.  
After your break, come back here and continue reading. The information below is brief, useful, and important.

#### Step 5: Heroku - Verify your account to spend more time with your new GRB.
- Accounts that use free dynos are allowed up to 550 hours of free dynos each month.
- That amount of time is enough to run GRB during Deep Town's weekend events.
- That amount of time is NOT enough to run GRB 24/7.

- IF YOU WISH, you may verify your Heroku account by placing a credit card or debit card on file with them.
	- Your card will never be charged unless you add something that costs money. YOU WILL KNOW if this happens.
	- Verified accounts that use free dynos are allowed up to 1,000 hours of free dynos each month.
	- That amount of time is enough to run GRB 24/7, with more than 25% left over.

The best place to get more information about this is directly from Heroku.

	https://devcenter.heroku.com/articles/account-verification

#### Step 6: GitHub - Configure computer for source code updates.
This section's title is misleading.  
This section contains no instructions at all.

This section is a reminder.  
Step 4 mentioned a separate guide titled "Updating GameResourceBot". The instructions you need to complete Step 6 are not here, but in that guide.  
The guide is available in three different flavours, for different operating systems.  
For more details, please see ["Updating GameResourceBot"](./README.md#updating-gameresourcebot).  
***OR*** go directly to the set of instructions for your computer's operating system:
- [MS Windows](./UPDATE-MSWin.md)
- [MacOS](./UPDATE-MacOS.md) - ***COMING SOON***
- [Linux](./UPDATE-Linux.md) - ***COMING SOON***

This section also is a warning.  
The NOT FIRST time you need to synchronise your forked repository with BlackCraze's master repository, you can accomplish the task in two minutes or less.

OK, to be honest, the FIRST time you do this, you still can accomplish the task in two minutes or less.  
The bad news: Before you reach the Promised Land of Two-Minute Synchronisation, you must tread through another setup process.  
The good news: That setup process is *much* shorter than this one.

[Back to top](#setting-up-gameresourcebot---long-version)

[Back to main `README` file](./README.md)

###### Footnote for Step 3
The Config Vars on Heroku are large and in charge. (That's why they're in ALL CAPS.)  
As mentioned in Step 3, a few of the Config Vars can be changed on Discord.  
But everybody needs a break occasionally, and GRB is no different.

Sometimes, GRB takes a break, meaning it reboots. This could be because:
- you updated the source code to give it new features (Later. [Much later.](./README.md#updating-gameresourcebot)); or
- you commanded it to shutdown and restart; or
- it jolly well felt like it. So there.

The point is, whenever GRB restarts, it always needs to know what its Config Vars are, and it always asks Heroku for this information. If you changed one or more of them on Discord before GRB rebooted, it will have no memory of this. (In that respect, restarting is like a really bad hangover. You know you need to remember what your buddy told you last night, but there's no way it's going to happen.) 

Therefore, if you do change one of the Config Vars on Discord later, think about whether you need that change to be permanent or not. If you do, then you'll probably want to return to Heroku and update that Config Var to match your changing need. If you decide not to return to Heroku, then don't say we didn't warn you.

[Back to spot where you left off](#this-line-does-not-exist-you-are-not-reading-this-please-move-on)