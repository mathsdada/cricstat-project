package rest;

import Database.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Controller.updateStatsDatabase();
        SpringApplication.run(Application.class, args);
    }
}
