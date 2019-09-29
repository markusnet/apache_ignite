import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class ComputeGrid implements Serializable {
    public static void main(String[] args) throws IgniteException {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            ignite.compute().broadcast(() -> System.out.println("Hello World!"));

            ClusterGroup clusterGroup = ignite.cluster().forRemotes();
            ignite.compute(clusterGroup).broadcast(() -> System.out.println("Hello World! from remotes"));

            Collection<Integer> res = ignite.compute().apply(
                    (String word) -> {
                        System.out.println("Counting characters in word '" + word + "'");

                        return word.length();
                    },
                    Arrays.asList("How many characters".split(" ")
                    ));

            int total = res.stream().mapToInt(Integer::intValue).sum();

            System.out.println("Total characters: " + total);
        }
    }
}