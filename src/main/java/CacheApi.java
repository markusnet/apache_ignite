import dto.Account;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

public class CacheApi {

    public static void main(String[] args) {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("default-config.xml")){
            CacheConfiguration<Integer, Account> cfg = new CacheConfiguration<>("demoCache");
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            IgniteCache<Integer, Account> cache = ignite.getOrCreateCache(cfg);

            cache.put(1, new Account(1, 100));
            cache.put(1, new Account(1, 100));

            System.out.println(cache.get(1));
            System.out.println(cache.get(2));

            cache.getAndPut(2 , new Account(22, 2200));

            IgniteFuture<Account> future = cache.getAsync(2);
            future.listen(f -> System.out.println("Retrieved cache value: " + future.get()));

            try (Transaction tx = ignite.transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                Account acct = cache.get(1);

                double balance = acct.getBalance();

                acct.setBalance(balance + 10);
                cache.getAndReplace(1, acct);
                tx.commit();
            }
            System.out.println("Account balance after deposit: " + cache.get(1));
        }
    }
}
