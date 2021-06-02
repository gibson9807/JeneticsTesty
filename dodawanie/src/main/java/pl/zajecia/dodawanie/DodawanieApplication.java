package pl.zajecia.dodawanie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DodawanieApplication {

    public static void main(String[] args) {
        SpringApplication.run(DodawanieApplication.class, args);
    }

    public int added(int a, int b){
        return a+b;
    }
}
