import org.apache.ignite.*;
import org.apache.ignite.internal.util.future.GridFutureAdapter;
import org.apache.ignite.internal.util.future.IgniteFutureImpl;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteInClosure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheDataStreamerExample {
    /** Cache name. */
    private static final String CACHE_NAME = CacheDataStreamerExample.class.getSimpleName();

    /** Number of entries to load. */
    private static final int ENTRY_COUNT = 500000;

    /** Heap size required to run this example. */
    public static final int MIN_MEMORY = 240 * 1024 * 1024;

    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If example execution failed.
     */
    public static void main(String[] args) throws IgniteException {
        ExamplesUtils.checkMinMemory(MIN_MEMORY);

        try (Ignite ignite = Ignition.start("file:///C:/Documents and Settings/Admin.HOME-PC/IdeaProjects/chapterone/src/main/java/config/example-ignite.xml")) {
            System.out.println();
            System.out.println(">>> Cache data streamer example started.");

            // Auto-close cache at the end of the example.
            try (IgniteCache<Integer, String> cache = ignite.getOrCreateCache(CACHE_NAME)) {


                long start = System.currentTimeMillis();


                try (IgniteDataStreamer<Integer, String> stmr = ignite.dataStreamer(CACHE_NAME)) {
                    // Configure loader.
                    stmr.perNodeBufferSize(1024);
                    stmr.perNodeParallelOperations(8);

                    for (int i = 0; i < ENTRY_COUNT; i++) {
                        stmr.addData(i, Integer.toString(i));

                        // Print out progress while loading cache.
                        if (i > 0 && i % 10000 == 0)
                            System.out.println("Loaded " + i + " keys.");
                    }
                }

                long end = System.currentTimeMillis();

                ExecutorService executor = ignite.executorService();

                List<Callable<Integer>> callables =  new ArrayList<>();

                final IgniteFuture<String> promise = new IgniteFutureImpl<>(new GridFutureAdapter<String>());

                cache.getAsync(5).listen(new IgniteInClosure<IgniteFuture<String>>() {
                    @Override public void apply(IgniteFuture<String> f) {
                        String val = f.get();

                        if (val != null) {
                            System.out.println("Completed by get: " + 5);

                            (((GridFutureAdapter)((IgniteFutureImpl)promise).internalFuture())).onDone("by get");
                        }
                    }
                });

                System.out.println(promise);


                System.out.println(">>> Loaded " + ENTRY_COUNT + " keys in " + (end - start) + "ms.");
            }
            finally {
                // Distributed cache could be removed from cluster only by #destroyCache() call.
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }
}


//entryMapToBeStreamed.entrySet().parallelStream().forEach(dataStreamer::addData);
//https://www.baeldung.com/apache-ignite
//https://ignite.apache.org/features/streaming.html
//https://www.youtube.com/channel/UCyFIhiuxO_YGBblieHpe90Q
//https://github.com/PacktPublishing/Apache-Ignite-Quick-Start-Guide
//https://play.google.com/books/reader?id=hC59DwAAQBAJ&hl=ru&pg=GBS.ZZ0