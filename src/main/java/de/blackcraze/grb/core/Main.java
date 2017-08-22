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
		Injector injector = Guice.createInjector(new DbUtil());
		initData(injector);
		initDiscord(injector);
	}

	private static void initData(Injector injector) {
		new StandingDataInitializer(injector).initStockTypes();
	}

	private static void initDiscord(Injector injector) {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		try {
			builder.setToken(readToken());
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

	private static String readToken() throws IOException {
		InputStream stream = Main.class.getClassLoader().getResourceAsStream("token.txt");
		return IOUtils.readLines(stream, Charset.forName("UTF-8")).get(0);
	}

}
