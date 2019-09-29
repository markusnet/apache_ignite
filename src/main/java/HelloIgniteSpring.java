import org.apache.ignite.Ignition;

/**
 * Created by shamim on 19/10/16.
 */
public class HelloIgniteSpring {
    public static void main(String[] args) {
        Ignition.start("default-config.xml");
    }
}
