package pl.zajecia.dodawanie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DodawanieApplication {

    public static void main(String[] args) {
        SpringApplication.run(DodawanieApplication.class, args);
    }

    public long added(long a, long b){
        return a+b;
    }
}
