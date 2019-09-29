import org.apache.ignite.IgniteLogger;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.java.JavaLogger;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Collections;

public class IgniteConfigurationEx {
    public static IgniteConfiguration exampleConfiguration() {
        int cpus = Runtime.getRuntime().availableProcessors();
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("simpleCache");
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setDeploymentMode(DeploymentMode.CONTINUOUS);
        cfg.setPeerClassLoadingMissedResourcesCacheSize(200);
        cfg.setPublicThreadPoolSize(4 * cpus);
        cfg.setSystemThreadPoolSize(2 * cpus);
        // log frequency in ms
        cfg.setMetricsLogFrequency(30000);
        cfg.setGridLogger(igniteLogger());
        cfg.setDiscoverySpi(multicastDiscoverySpi());
        return cfg;
    }

    private static DiscoverySpi multicastDiscoverySpi() {
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        spi.setIpFinder(multicastIpFinder());
        return null;
    }

    private static TcpDiscoveryIpFinder multicastIpFinder() {
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("localhost:47500"));
        return ipFinder;
    }

    private static IgniteLogger igniteLogger() {
        return new JavaLogger();
    }
}
