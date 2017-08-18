package de.blackcraze.grb.commands;

import java.util.List;

public abstract class AbstractCommand {

	abstract List<String> getChannels();

	abstract boolean trigger();

}
