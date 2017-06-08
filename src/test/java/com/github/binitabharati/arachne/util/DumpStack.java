package com.github.binitabharati.arachne.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DumpStack {
	
	// directory where the stack files are written
    private static final String STACK_DUMP_DIR = "/var/tmp";

    // here for testing
    public static void main(String[] args) throws Exception {
            dumpStacks();
    }

    public static void dumpStacks() throws IOException {
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfos = mxBean.getThreadInfo(mxBean.getAllThreadIds(), 0);
            Map<Long, ThreadInfo> threadInfoMap = new HashMap<Long, ThreadInfo>();
            for (ThreadInfo threadInfo : threadInfos) {
                    threadInfoMap.put(threadInfo.getThreadId(), threadInfo);
            }

            // choose our dump-file
            File dumpFile = new File(STACK_DUMP_DIR, "stacks." + System.currentTimeMillis());
            Writer writer = new BufferedWriter(new FileWriter(dumpFile));
            try {
                    dumpTraces(mxBean, threadInfoMap, writer);
            } finally {
                    writer.close();
            }
    }

    private static void dumpTraces(ThreadMXBean mxBean, Map<Long, ThreadInfo> threadInfoMap, Writer writer)
                    throws IOException {
            Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
            writer.write("Dump of " + stacks.size() + " thread at "
                            + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z").format(new Date(System.currentTimeMillis())) + "\n\n");
            for (Map.Entry<Thread, StackTraceElement[]> entry : stacks.entrySet()) {
                    Thread thread = entry.getKey();
                    writer.write("\"" + thread.getName() + "\" prio=" + thread.getPriority() + " tid=" + thread.getId() + " "
                                    + thread.getState() + " " + (thread.isDaemon() ? "deamon" : "worker") + "\n");
                    ThreadInfo threadInfo = threadInfoMap.get(thread.getId());
                    if (threadInfo != null) {
                            writer.write("    native=" + threadInfo.isInNative() + ", suspended=" + threadInfo.isSuspended()
                                            + ", block=" + threadInfo.getBlockedCount() + ", wait=" + threadInfo.getWaitedCount() + "\n");
                            writer.write("    lock=" + threadInfo.getLockName() + " owned by " + threadInfo.getLockOwnerName()
                                            + " (" + threadInfo.getLockOwnerId() + "), cpu="
                                            + (mxBean.getThreadCpuTime(threadInfo.getThreadId()) / 1000000L) + ", user="
                                            + (mxBean.getThreadUserTime(threadInfo.getThreadId()) / 1000000L) + "\n");
                    }
                    for (StackTraceElement element : entry.getValue()) {
                            writer.write("        ");
                            writer.write(element.toString());
                            writer.write("\n");
                    }
                    writer.write("\n");
            }
    }

}
