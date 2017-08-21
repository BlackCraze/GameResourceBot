package de.blackcraze.grb.util;

import org.apache.commons.lang3.StringUtils;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Utils {

	public static final boolean botMentioned(GuildMessageReceivedEvent event) {
		return StringUtils.startsWithIgnoreCase(event.getMessage().getContent(), "bot");
		// for (Role role : event.getMessage().getMentionedRoles()) {
		// if (role.getName().equalsIgnoreCase("bot")) {
		// return true;
		// }
		// }
		// return false;
	}

}
