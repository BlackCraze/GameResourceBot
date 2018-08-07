### Hello, friendly developers!!

HERE IT IS!!
Your favourite part of the entire development process:
# The Stupid User Check :-D
###### HINT: His handle is PellaAndroid. :-P

As you know, the Setup and Update guides here were written by a user with a good amount of technical knowledge about how things work. However, he is not a developer, so you have to keep him in line.

Below, you will find some of the section headers from the aforementioned Setup and Update guides, along with excerpts of those guides below their relevant headers. These excerpts are the parts where the author of the guides explains highly technical terms and concepts to lay users (the EXTREMELY non-technical ones).

Your mission, should you choose to accept it, is to read through the author's explanations and make sure they accurately explain the things they are supposed to explain.
> The explanations you are judging will always appear as quoted text, like this.

##### ACCURATELY, not necessarily ADEQUATELY

These explanations are supposed to be super-simple, so don't be concerned about the explanations being complete. As long as the correct meaning is communicated, that's all that matters.

By the way, THANK YOU SO MUCH for everything you have done and continue to do for this project.

### Setting Up GameResourceBot
#### 1. Discord - Create a new app.
1. In the middle, after Action #3
    - The app and bot user have been created on Discord. Next is this text:
> We have an app. The app has a bot. GRB wants to move in to its new home, but it doesn't know where home is.
  We must create a special link that will act like GPS for GRB.
2. At the end, Action #8:
> 8. Go to your guild's Discord server.
  	- Hooray! GRB found its new home and has already moved in.
  	- However, we can't interact with it yet. After moving and unpacking, GRB needs a nap.
  	- If you added an APP ICON earlier, you will not see it yet. GRB will hang its pictures when it wakes up.
  	- Unfortunately, the moving van did not bring GRB's brain. We will build its brain in Step 2.

#### 2. GitHub - Fork the code to a new repository on your account.
1. First thing:
> Our sleeping GRB needs a brain, a set of instructions for its duties. Those instructions are in the source code.
  BlackCraze keeps the master source code on his account. For your GRB, you will need your own copy of the source code.
To accomplish this, you must duplicate the master source code to your account.
> - Your copy of the source code is called a "repository".
> - The process of duplicating the source code is called "forking". Each existing copy of the source code (other than the master copy that BlackCraze keeps) is called a "fork".
> - A repository can contain original code (BlackCraze) or it can be a fork (you, and others before you).
2. Last thing:
> We have a GRB. We have a brain. We do not have a battery pack to power the brain--yet.
  We're done here. Moving on.

#### Step 3: Heroku - Deploy the GRB app.
1. First thing:
> Welcome to the host site, our battery pack.
Every application on the Web requires a host. The host has several functions.
> - The source code is in plain text that humans can read. Computers can read it, too, but not very well. Computers read plain text extremely slowly, almost like a kindergartener learning to read.
> - The host converts the plain text into the 1s and 0s that computers prefer. This conversion process is called "compiling".
> - Compiling the source code is like welding the battery pack into the brain. The host puts the different parts together into a single unit that computers can read extremely quickly. This single unit **IS** the app, which is our GRB.
> - After it compiles the source code, the host runs the GRB app. But we don't want to interact with GRB here. We want to go to GRB's new home, wake it up, and have a big housewarming party.
> - Therefore, the host (Heroku) and GRB (at home, on Discord) send data back and forth to each other through the Internet.
> - That is, they WILL. They can't yet, because they haven't met yet. Our task here is to introduce them to each other.

#### Step 4: Heroku - Configure for automatic updates. Start GRB. Configure dynos.
1. First thing:
> We created a home for GRB on Discord.
On GitHub, we made our own copy of GRB's source code.
Heroku compiled the source code and deployed the GRB app--brain, battery pack, and all.
GRB and Heroku have made formal introductions and are ready to begin sending data back and forth. All they need is for you to say, "Go!"

> And you will, very soon. But first, we have a bit of unfinished business to complete.
> - Occasionally, BlackCraze updates the master source code with new features, bug fixes, and other things.
> - After that happens, you will need to update your GitHub repository with the new source code.
> - After THAT happens, Heroku must retrieve the new source code, compile it, and redeploy the GRB app.
> - Here's the cool part: Heroku can do all of that automatically, without any human interaction.

2. Part of #2:
> 	- The first section is "Free Dynos".
  		- Dynos are like small plastic containers in a refrigerator.
  		- All the different apps on Heroku are like food in the containers.
  		- The dynos protect the apps from external things that can cause problems, and from each other.
  		- Dynos come in two main categories: Free and Not Free. GRB LOVES the Free ones!

### Updating GameResourceBot
## Important Background Information
1. All of it:
> During Step 4 of the GRB Setup, we mentioned briefly that BlackCraze occasionally updates the master source code, which is in his master repository.
During that same step, we configured Heroku to deploy a shiny new version of GRB every time the source code on GitHub changes. But there's a catch.

> Your Heroku account is not connected to BlackCraze's GitHub account. It's connected to YOUR GitHub account.
Therefore, the only way for you to get a shiny new GRB is to synchronise your forked repository with BlackCraze's master repository.

> Unlike Heroku, GitHub does not have a way to make this happen without human interaction.
This is because GitHub is a really busy place. Imagine if someone attempted to set up a picnic in the traffic lanes of a major freeway during rush hour. The result would be bad for the picnickers and for the motorists, and it still would be LESS chaotic than GitHub with an auto-synchronise function. Trust us, you don't want that.

#### STEP 3: CLONE YOUR REPOSITORY
1. First thing:
> Because of the unique ways that GitHub works, the only way to synchronise your forked repository with BlackCraze's master repository is to create a duplicate of your repository on your computer's hard drive.
> - Yes, it probably seems weird to you. It seems weird to most people, but it's how GitHub works.
> - Sometimes, other people help BlackCraze write the source code.
> - A helper often keeps a working copy of their repository on their computer's hard drive.
> - The helper then transfers their work from their working copy to the master repository.
> - You are going to pretend to be a helper. This actually simplifies things for GitHub.
> - This allows GitHub to use one update method for helpers and non-helpers, instead of two methods.

# That's all!
## See? Wasn't that quick and easy? ;-)