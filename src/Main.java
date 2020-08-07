import java.util.ArrayList;

import static sun.jvm.hotspot.runtime.PerfMemory.start;

public class Main {

    public static void main(String[] args) {
        FIFOQueue queue = new FIFOQueue();
        int numOfThreads = 3;
        int items = 4;
        Object printSynchronize = new Object();

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
                    }
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    for (int j = 0; j <= items; j++) {
                        try {
                            int val = queue.dequeue();
                            System.out.println(String.format("%d::%d", tid, val));//

                        }
                        catch (EmptyQueueException e) {
                            System.out.println(String.format("%d::%s", tid, e.toString()));
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
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
