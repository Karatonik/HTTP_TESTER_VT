package pl.kalksztejn.mateusz.httpmethodstester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HttpMethodsTesterApplication {

    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(HttpMethodsTesterApplication.class, args);
    }

    public static void shutdown() {
        if (applicationContext != null) {
            SpringApplication.exit(applicationContext, () -> 0);
        }
    }

    // Dodajemy hook do zamknięcia aplikacji przed zakończeniem
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(HttpMethodsTesterApplication::shutdown));
    }

}
