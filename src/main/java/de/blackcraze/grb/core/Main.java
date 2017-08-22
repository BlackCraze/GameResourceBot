package de.blackcraze.grb.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

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

		String token = null;
		try {
			token = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("you have to provide a bot token code when starting like this: ");
			System.err.println("\tjava -jar grb.jar thisIsMyDiscordTokenCode");
			System.exit(1);
		}
		Injector injector = Guice.createInjector(new DbUtil());
		initData(injector);
		initDiscord(injector, token);
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
