package de.blackcraze.grb.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.blackcraze.grb.listener.MessageListener;
import de.blackcraze.grb.listener.ReadyListener;
import de.blackcraze.grb.util.DbUtil;
import de.blackcraze.grb.util.StandingDataInitializer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

public class Main {

	public static void main(String[] args) {

		for (String env_var: BotConfig.REQUIRED_ENV_VARS) {
			if (System.getenv(env_var) == null) {
				System.err.printf("Missing environment variable: \"%s\"%n", env_var);
				System.exit(1);
			}
		}

		Injector injector = Guice.createInjector(new DbUtil());
		initData(injector);
		initDiscord(injector, BotConfig.DISCORD_TOKEN);
	}

	private static void initData(Injector injector) {
		new StandingDataInitializer(injector).initStockTypes();
	}

	private static void initDiscord(Injector injector, String token) {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		try {
			builder.setToken(token);
			builder.setAutoReconnect(true);
			builder.setStatus(OnlineStatus.ONLINE);
			builder.addEventListener(new ReadyListener(injector));
			builder.addEventListener(new MessageListener(injector));

			builder.buildBlocking();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			builder.setStatus(OnlineStatus.OFFLINE);
		}
	}

}
