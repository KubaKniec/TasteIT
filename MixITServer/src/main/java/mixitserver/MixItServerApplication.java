package mixitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class MixItServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MixItServerApplication.class, args);
    }
}
