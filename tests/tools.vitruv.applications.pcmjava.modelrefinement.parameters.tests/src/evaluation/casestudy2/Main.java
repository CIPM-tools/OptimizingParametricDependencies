package evaluation.casestudy2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;
import kieker.monitoring.sampler.sigar.samplers.CPUsDetailedPercSampler;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.KiekerMonitoringRecordsWriter;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.MonitoringRecordsCache;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class Main {

    final static ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;

    final static CPUsDetailedPercSampler cpuSampler = sigarFactory.createSensorCPUsDetailedPerc();

    private static MonitoringRecordsCache cache;
    private static int CACHE_SIZE = 100000;

    public static void main(final String[] args) throws Exception {

        System.out.println("Starting now.");
        System.out.flush();
        MonitoringController.getInstance().schedulePeriodicSampler(cpuSampler, 0, 1, TimeUnit.SECONDS);

        System.out.println("Set processor affinity to CPU 0 and press enter.");
        System.in.read();

        KiekerMonitoringRecordsWriter writer = new KiekerMonitoringRecordsWriter(MonitoringController.getInstance());

        cache = new MonitoringRecordsCache(CACHE_SIZE, writer);

        ThreadMonitoringController.setMonitoringRecordsWriter(writer);

        evaluationRun(50,0,10,"session-1");

        System.out.println("Finished performance monitoring.");
    }

    private static void evaluationRun(int iterations, int thinkTimeMillis, int population, String name) throws Exception {
        Common c = new Common(name, Mode.Nothing);
        

        ThreadMonitoringController.setSessionId(c.name);
        evaluationThreadedRun(c,iterations, thinkTimeMillis, population);

        if (cache.getItemsCount() > CACHE_SIZE) {
            System.err.println(String.format("The cache exeeded the initial size. Items in cache was: %d.",
                    cache.getItemsCount()));
        }

        cache.flushCache();
        System.out.flush();
        System.err.flush();
    }

    private static void evaluationThreadedRun(Common c, int iterations, int thinkTimeMillis, int population)
            throws Exception {

        Runnable task = new Runnable() {

            @Override
            public void run() {
                try {
                    this.internRun();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }

            private void internRun() throws Exception {
                Random rand = ThreadLocalRandom.current();

                if (thinkTimeMillis > 0) {
                    Thread.sleep(rand.nextInt(thinkTimeMillis));
                }

                B b = new B(c);
                A a = new A(b, c);

                for (int k = 0; k < iterations; k++) {

                    int i = rand.nextInt(10);
                    int j = 1+rand.nextInt(3);
                    boolean bool = rand.nextBoolean();
                    a.methodA(i, j, bool);

                    if (thinkTimeMillis > 0) {
                        Thread.sleep(thinkTimeMillis);
                    }

                    if (k % 100 == 0) {
                        System.out.println(
                                "Iteration: " + String.valueOf(k));
                    }
                }
            }
        };

        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < population; i++) {
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
