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
import net.dv8tion.jda.core.entities.Message;

public class Status implements BaseCommand {
    public void run(Scanner scanner, Message message) {
        StringBuilder sb = new StringBuilder();
        Locale locale = getResponseLocale(message);
        /* BLACKCRAZE: I created headers for i18n here because this is a command that any user CAN use, even if they
            probably won't. Instead of using %d within the strings, I kept your "append" format. Because of this,
            several of the strings BEGIN with %n. My intent was to reduce clutter from the .append("\n") code.
            If that won't work, then, obviously, you'll want to fix it.
            Alternatively, your original code is here, in case you think we shouldn't bother with this one.
            I still maintain my opinion, though, that we should translate anything that isn't exclusively in the logs.
         */
        sb.append(Resource.getHeader("MY_MEM", locale));
        /* ORIGINAL VERSION OF PREVIOUS LINE BELOW
        sb.append("My Memory:\n\n");
         */
        sb.append("\n\n");
        for (MemPool pool : getPools()) {
            MemoryUsage usage = pool.getUsage();
            sb.append(pool.getName());
            sb.append(Resource.getHeader("INIT_HEAD", locale))
                    .append(FileUtils.byteCountToDisplaySize(usage.getInit()));
            sb.append(Resource.getHeader("USED", locale))
                    .append(FileUtils.byteCountToDisplaySize(usage.getUsed()));
            sb.append(Resource.getHeader("COMMITTED", locale))
                    .append(FileUtils.byteCountToDisplaySize(usage.getCommitted()));
            sb.append(Resource.getHeader("MAX", locale))
                    .append(FileUtils.byteCountToDisplaySize(usage.getMax())).append("\n");
            /* ORIGINAL VERSION OF PREVIOUS 5 LINES BELOW
            sb.append(pool.getName()).append("\n");
            sb.append("\tINIT:     ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getInit())) .append("\n");
            sb.append("\tUSED:     ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getUsed())) .append("\n");
            sb.append("\tCOMMITED: ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getCommitted())).append("\n");
            sb.append("\tMAX:      ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getMax())) .append("\n");
             */
        }
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
        /* ORIGINAL VERSION OF PREVIOUS 2 LINES BELOW
        result.add(new MemPool("Heap", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()));
        result.add(new MemPool("Non-Heap",
                ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
        */
        for (MemoryPoolMXBean poolMXBean : poolMXBeans) {
            result.add(new MemPool(poolMXBean.getName(), poolMXBean.getUsage()));
            /* OHHHHhhhhhh! Isn't THAT interesting? I've created i18n for the four subtitles of each memory space,
            but the names of the memory spaces themselves appear to be generated elsewhere, and therefore cannot
            be translated. hmmmmm.....
            Well, I'll leave the decision about how to handle this up to you. My opinion above remains unchanged,
            though. If there's a way to display the names of the memory spaces in other languages, I think we should
            make the effort. And in this case, by "we", I mean "you", since I have no clue how to help with that. ;-)
            */
        }
        return result;
    }

}
