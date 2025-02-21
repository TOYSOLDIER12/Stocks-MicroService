package ma.xproce.stocksmicroservice;

import io.github.cdimascio.dotenv.Dotenv;
import ma.xproce.stocksmicroservice.Services.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient
@SpringBootApplication
public class StocksMicroServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().directory("./Stocks-MicroService").load();
        System.setProperty("MYSQL_SOURCE", dotenv.get("MYSQL_SOURCE"));
        System.setProperty("MYSQL_PORT", dotenv.get("MYSQL_PORT"));
        System.setProperty("MYSQL_USERNAME", dotenv.get("MYSQL_USERNAME"));
        System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
        System.setProperty("AUTH_SERVER", dotenv.get("AUTH_SERVER"));
        System.setProperty("AUTH_PORT", dotenv.get("AUTH_PORT"));

        SpringApplication.run(StocksMicroServiceApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }
   @Autowired
    StockManager stockManager;
    @Override
    public void run(String... args) throws Exception {
        stockManager.populateEnterprise();
    }


}
