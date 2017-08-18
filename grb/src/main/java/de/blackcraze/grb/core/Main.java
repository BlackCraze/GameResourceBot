package de.blackcraze.grb.core;

import javax.security.auth.login.LoginException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.blackcraze.grb.listener.MessageListener;
import de.blackcraze.grb.listener.ReadyListener;
import de.blackcraze.grb.util.DbUtil;
import de.blackcraze.grb.util.Secrets;
import de.blackcraze.grb.util.StandingDataInitializer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

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
		builder.setToken(Secrets.TOKEN);
		builder.setAutoReconnect(true);
		builder.setStatus(OnlineStatus.ONLINE);
		builder.addEventListener(new ReadyListener(injector));
		builder.addEventListener(new MessageListener(injector));

		try {
			builder.buildBlocking();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		} finally {
			builder.setStatus(OnlineStatus.OFFLINE);
		}
	}

}
