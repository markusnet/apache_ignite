package threads;

import org.apache.ignite.IgniteCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class FirstJob implements Runnable {

    private IgniteCache<Long, Long> cache;

    private final AtomicReference<Object> res;

    public FirstJob(IgniteCache<Long, Long> cache, AtomicReference<Object> res) {
        this.cache = cache;
        this.res = res;
    }

    @Override
    public void run() {
        System.out.println("Will try to lock key with 500 ms timeout (should fail).");

        try {
            boolean b = cache.lock(1L).tryLock(500, TimeUnit.MILLISECONDS);
            if (!b) {
                res.set(false);
            } else {
                System.exit(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
