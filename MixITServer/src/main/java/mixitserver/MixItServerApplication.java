package mixitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
@SpringBootApplication
public class MixItServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MixItServerApplication.class, args);
    }

}
