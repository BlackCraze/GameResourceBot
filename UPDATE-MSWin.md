# Updating GameResourceBot (MS Windows)
######  v1.0, by PellaAndroid

*GameResourceBot has a nickname. It likes to be called "GRB", so we call it that most of the time.*

***FIRST-TIMERS: SKIP THIS PART***  
If you previously completed all steps in this guide and need to update again, please [skip to STEP 4](#step-4-synchronise-your-repository-aka-the-actual-update-process).  
***FIRST-TIMERS: SKIP THIS PART***

### CONTENTS
1. [About This Guide](#about-this-guide)
2. [Important Background Information](#important-background-information)
3. [The (Setup to) Update Process](#the-setup-to-update-process)  
    (1) [STEP 1. INSTALL GITHUB DESKTOP](#step-1-install-github-desktop)  
    (2) [STEP 2. LOG IN TO GITHUB](#step-2-log-in-to-github)  
    (3) [STEP 3. CLONE YOUR REPOSITORY](#step-3-clone-your-repository)  
    (4) [STEP 4. SYNC YOUR REPOSITORY](#step-4-synchronise-your-repository-aka-the-actual-update-process)

## About This Guide
The GRB setup process you already encountered is the same for everyone. The GRB update process here is not.  
Which GRB update process you will use depends on the operating system your computer uses.  
If your computer's operating system is Microsoft Windows 7 or later (64-bit), this guide is for you.  
If your computer's operating system is something else, please see ["Updating GameResourceBot"](./README.md#updating-gameresourcebot).

This guide assumes you are familiar with the guide titled ["Setting Up GameResourceBot"](./SETUP-long.md), meaning you have gone through all the steps in that guide and have a general understanding of all the technical terms in that guide. Otherwise, why would you even be reading this guide? Therefore, if you encounter something in this guide that you do not understand and is not explained in this guide, please refer to that guide.

Finally, all the headers for the primary step numbers in this guide (the final four lines of [CONTENTS](#contents)) use ALL CAPS. (See? We didn't forget about the important clue we promised you!) This helps you make sure you're actually using the guide you want to be using.

## Important Background Information
During [Step 4 of "Setting Up GRB"](./SETUP-long.md#step-4-heroku---configure-for-automatic-updates-start-grb-configure-dynos), we mentioned briefly that BlackCraze occasionally updates the master source code, which is in his master repository.  
During that same step, we configured Heroku to deploy a shiny new version of GRB every time the source code on GitHub changes. But there's a catch.

Your Heroku account is not connected to BlackCraze's GitHub account. It's connected to YOUR GitHub account.  
Therefore, the only way for you to get a shiny new GRB is to synchronise your forked repository with BlackCraze's master repository.

Unlike Heroku, GitHub does not have a way to make this happen without human interaction.  
This is because GitHub is a really busy place. Imagine if someone attempted to set up a picnic in the traffic lanes of a major freeway during rush hour. The result would be bad for the picnickers and for the motorists, and it still would be LESS chaotic than GitHub with an auto-synchronise function. Trust us, you don't want that.

## The (Setup to) Update Process
#### STEP 1: INSTALL GITHUB DESKTOP
1. Start your web browser.
	1. Go to https://desktop.github.com/
	2. Large button in the centre: Download for Windows (64bit)  
Setup app will download.
	3. Locate the file you just downloaded (GitHubDesktopSetup.exe).
	    1. Double-click the file to begin the setup.
	    2. After setup, the GitHub Desktop app should start automatically. If it doesn't, start it yourself.

#### STEP 2: LOG IN TO GITHUB
There are two different ways to log in. You need only one (if it is successful).
    
##### SUPER-EASY METHOD
1. Start GitHub Desktop.
	1. Menu bar: File > Options...
	2. Top left: Accounts
	3. Blue link near the bottom: Sign in using your browser
2. Switch to your web browser.  
ATTENTION: Specifics will vary, depending on the browser you are using.
	1. Near the bottom: Check "Remember my choice for x-github-client links".
	2. In the centre of the pop-up window, select GitHubDesktop.exe
	3. Bottom right: Open link
3. Switch to GitHub Desktop.
	1. Menu bar: File > Options...
	2. If you see your GitHub username here, you're good. Skip to [STEP 3](#step-3-clone-your-repository).
	3. If you don't see your GitHub username here, proceed with [EASY METHOD](#easy-method).

##### EASY METHOD
1. Start GitHub Desktop.
	1. Menu bar: File > Options...
	2. Top left: Accounts
	3. Far right, upper blue button: Sign in
	4. Enter your GitHub login credentials.
	5. Blue button at bottom: Sign in
	6. Menu bar: File > Options...
	7. If you see your GitHub username here, you're good. Skip to [STEP 3](#step-3-clone-your-repository).
	8. If you don't see your GitHub username here, something is wrong. Begin [STEP 2](#step-2-log-in-to-github) again.

#### STEP 3: CLONE YOUR REPOSITORY
Because of the unique ways that GitHub works, the only way to synchronise your forked repository with BlackCraze's master repository is to create a duplicate of your repository on your computer's hard drive.
- Yes, it probably seems weird to you. It seems weird to most people, but it's how GitHub works.
- Sometimes, other people help BlackCraze write the source code.
- A helper often keeps a working copy of their repository on their computer's hard drive.
- The helper then transfers their work from their working copy to the master repository.
- You are going to pretend to be a helper. This actually simplifies things for GitHub.
- This allows GitHub to use one update method for helpers and non-helpers, instead of two methods.


1. Switch to your web browser.
	1. Go to https://www.github.com
	2. Scroll to the bottom of the page.
	3. Bottom right: Section "Your repositories": Click GameResourceBot
	4. Far right: Green button: Clone or download
	5. Bottom left of small pop-up window: Open in Desktop
2. GitHub Desktop (Windows should switch here automatically. If not, go there.)  
Local path: The default local path begins with your user directory, then adds `\Documents\GitHub\GameResourceBot`
	1. Recommended: Keep the default.
	2. ***OR*** If you prefer a different path, click Choose... and select the folder you prefer.
	3. Blue button: Clone
	4. Watch the magic happen.

Congratulations! The hardest part is done. In the next step, we will synchronise your forked repository with the master repository. For future updates, the next step is the only step you will need to do.

#### STEP 4: SYNCHRONISE YOUR REPOSITORY  (a.k.a. THE ACTUAL UPDATE PROCESS)
1. Start or Switch to GitHub Desktop (if needed).
	1. Below the menu bar, second button should read: Current branch: master
		1. If it does not, click it and select "master".
        Menu bar: Repository > Pull
		2. ***OR*** Large button below the menu bar, on the right: Fetch origin
	2. Menu bar: Branch > Merge into current branch...
		1. Bottom: Other branches: Select upstream/master
		2. Blue button at bottom: Merge into master
	3. Menu bar: Repository > Push
	4. ***OR*** Large button below the menu bar, on the right: Push origin

That's it! Heroku will now see the changes and update your GRB.  
Enjoy!

[Back to top](#updating-gameresourcebot-ms-windows)

[Back to main `README` file](./README.md)
