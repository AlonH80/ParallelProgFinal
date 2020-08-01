import java.util.ArrayList;

import static sun.jvm.hotspot.runtime.PerfMemory.start;

public class Main {

    public static void main(String[] args) {
        FIFOQueue queue = new FIFOQueue();
        int numOfThreads = 3;
        int items = 5;

        ArrayList<Thread> ths = new ArrayList<>(numOfThreads);
        int i;
        for(i = 0; i < numOfThreads; i++) {
            try {
                int finalI = i;
                long tid = i;
                System.out.println(String.format("Start thread %d", tid));
                ths.add(new Thread(() -> {
                    for (int j = items * finalI; j < items * (finalI + 1); j++) {
                        queue.enqueue(j);
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (int j = 0; j < items + 1; j++) {
                        System.out.println(String.format("%d::%d", tid, queue.dequeue()));
                    }
                })
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ths.forEach(Thread::start);
    }
}
