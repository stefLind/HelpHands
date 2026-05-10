package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HelpHandsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpHandsApplication.class, args);
    }

}
