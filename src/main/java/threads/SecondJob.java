package threads;

import org.apache.ignite.IgniteCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public class SecondJob implements Runnable {

    private IgniteCache<Long, Long> cache;

    private final AtomicReference<Object> res;

    public SecondJob(IgniteCache<Long, Long> cache, AtomicReference<Object> res) {
        this.cache = cache;
        this.res = res;
    }

    @Override
    public void run() {
        System.out.println("Will try to lock key with 500 ms timeout (should fail).");

        Lock lock = cache.lock(1L);

        try {
            lock.lock();
            res.set(true);
        } finally {
            lock.unlock();
        }
    }
}
