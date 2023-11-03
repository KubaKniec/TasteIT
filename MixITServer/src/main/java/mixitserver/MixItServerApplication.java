package mixitserver;

import mixitserver.api.Fetcherv2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.io.IOException;
//KONIECZNIE USUNCIE TO CO PO SpringBootApplication JESLI CHCECIE URUCHOMIC SERWER
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@SpringBootApplication
public class MixItServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MixItServerApplication.class, args);
//        Fetcherv2 f = new Fetcherv2();
//        System.out.println("Started fetching drinks.....");
//        f.fetchAll();
//        System.out.println(f.getDrinks());
//        System.out.println("Done!");


    }

}
