package de.blackcraze.grb.commands.concrete;

import static de.blackcraze.grb.util.CommandUtils.getResponseLocale;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import de.blackcraze.grb.i18n.Resource;
import net.dv8tion.jda.api.entities.Message;

public class Status implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        StringBuilder sb = new StringBuilder();
        Locale locale = getResponseLocale(message);
        sb.append("My Memory:\n\n");
        sb.append("\n\n");
        for (MemPool pool : getPools()) {
            MemoryUsage usage = pool.getUsage();
            sb.append(pool.getName()).append("\n");
            sb.append("\tINIT:      ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getInit())) .append("\n");
            sb.append("\tUSED:      ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getUsed())) .append("\n");
            sb.append("\tCOMMITTED: ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getCommitted())).append("\n");
            sb.append("\tMAX:       ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getMax())) .append("\n");
        }
        // Next line added by PellaAndroid to keep nosy users away from this command.
        Speaker.say(message.getChannel(), Resource.getInfo("WHY_ARE_YOU_HERE", locale));
        Speaker.sayCode(message.getChannel(), sb.toString());
    }

    static class MemPool {

        private final String name;

        private final MemoryUsage usage;

        MemPool(String name, MemoryUsage usage) {
            this.name = name;
            this.usage = usage;
        }

        public String getName() {
            return name;
        }

        MemoryUsage getUsage() {
            return usage;
        }
    }

    private static List<MemPool> getPools() {
        List<MemoryPoolMXBean> poolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        List<MemPool> result = new ArrayList<>(poolMXBeans.size() + 2);
        result.add(new MemPool("Heap", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()));
        result.add(new MemPool("Non-Heap",
                ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
        for (MemoryPoolMXBean poolMXBean : poolMXBeans) {
            result.add(new MemPool(poolMXBean.getName(), poolMXBean.getUsage()));
        }
        return result;
    }

}
