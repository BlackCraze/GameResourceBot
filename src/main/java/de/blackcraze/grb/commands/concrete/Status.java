package de.blackcraze.grb.commands.concrete;

import de.blackcraze.grb.commands.BaseCommand;
import de.blackcraze.grb.core.Speaker;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.FileUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Status implements BaseCommand{
    public void run(Scanner scanner, Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("My Memory:\n\n");
        sb.append("\n\n");
        for (MemPool pool : getPools()) {
            MemoryUsage usage = pool.getUsage();
            sb.append(pool.getName()).append("\n");
            sb.append("\tINIT:     ").append(FileUtils.byteCountToDisplaySize(usage.getInit()))
                    .append("\n");
            sb.append("\tUSED:     ").append(FileUtils.byteCountToDisplaySize(usage.getUsed()))
                    .append("\n");
            sb.append("\tCOMMITED: ")
                    .append(FileUtils.byteCountToDisplaySize(usage.getCommitted())).append("\n");
            sb.append("\tMAX:      ").append(FileUtils.byteCountToDisplaySize(usage.getMax()))
                    .append("\n");
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
        result.add(new MemPool("Non-Heap", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()));
        for (MemoryPoolMXBean poolMXBean : poolMXBeans) {
            result.add(new MemPool(poolMXBean.getName(), poolMXBean.getUsage()));
        }
        return result;
    }

}
