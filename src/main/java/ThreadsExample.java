import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.CacheConfiguration;
import threads.FirstJob;
import threads.SecondJob;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public class ThreadsExample {
    private static final String CACHE_NAME = ThreadsExample.class.getSimpleName();

    public static void main(String[] args) throws IgniteException {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            System.out.println();
            System.out.println(">>> Cache lock example started.");

            CacheConfiguration<Long, Long> cc = new CacheConfiguration<>(CACHE_NAME);

            cc.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            try (IgniteCache<Long, Long> cache = ignite.createCache(cc)){
                Lock lock = cache.lock(1L);
                lock.lock();

                final AtomicReference<Object> res = new AtomicReference<>();

                FirstJob firstJob = new FirstJob(cache, res);
                Thread thread1 = new Thread(firstJob);
                thread1.start();

                while (res.get() == null) {
                    System.out.println("Waiting for operation to complete in parallel thread.");

                    Thread.sleep(500);
                }

                if (res.get() instanceof Boolean) {
                    System.out.println("Locking in parallel thread failed: " + res.get());
                } else {
                    throw new Exception("Unexpected result: " + res.get());
                }
                res.set(null);
                lock.unlock();

                SecondJob secondJob = new SecondJob(cache, res);
                Thread thread2 = new Thread(secondJob);
                thread2.start();
                while (res.get() == null) {
                    System.out.println("Waiting for operation to complete in parallel thread.");

                    Thread.sleep(500);
                }

                if (res.get() instanceof Boolean) {
                    System.out.println("Locking in parallel thread succeeded: " + res.get());
                } else {
                    throw new Exception("Unexpected result: " + res.get());
                }

                thread2.join();
                System.out.println(">>> Cache lock example finished.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}