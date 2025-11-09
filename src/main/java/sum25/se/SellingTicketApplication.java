package sum25.se;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SellingTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellingTicketApplication.class, args);
    }

}
