package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    //TODO: Falta hacer la conneccion a loquibase o configurarlo, changelog mas table changes.
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
