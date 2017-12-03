package de.blackcraze.grb.core;

import de.blackcraze.grb.listener.MessageListener;
import de.blackcraze.grb.listener.ReadyListener;
import de.blackcraze.grb.util.StandingDataInitializer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import java.util.Objects;

public class Main {

	public static void main(String[] args) {

		for (String env_var: BotConfig.REQUIRED_ENV_VARS) {
			if (Objects.isNull(System.getenv(env_var))) {
				System.err.printf("Missing environment variable: \"%s\"%n", env_var);
				// replace hard-coded message with MISSING_ENV_VAR
				System.exit(1);
			}
		}

		initData();
		initDiscord();
	}

	private static void initData() {
		new StandingDataInitializer().initStockTypes();
	}

	private static void initDiscord() {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		try {
			builder.setToken(BotConfig.DISCORD_TOKEN);
			builder.setAutoReconnect(true);
			builder.setStatus(OnlineStatus.ONLINE);
			builder.addEventListener(new ReadyListener());
			builder.addEventListener(new MessageListener());

			builder.buildBlocking();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			builder.setStatus(OnlineStatus.OFFLINE);
		}
	}

}
