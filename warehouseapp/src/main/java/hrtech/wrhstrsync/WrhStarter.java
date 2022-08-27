package hrtech.wrhstrsync;

import hrtech.wrhstrsync.model.order.Order;
import hrtech.wrhstrsync.model.product.Product;
import hrtech.wrhstrsync.repository.IOrderRepository;
import hrtech.wrhstrsync.repository.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@SpringBootApplication
public class WrhStarter {

    private final Logger logger = LoggerFactory.getLogger(WrhStarter.class);


    public static void main(String[] args) {
        SpringApplication.run(WrhStarter.class, args);
    }


    @Bean
    @Profile("dev")
    public CommandLineRunner bootstrap(IProductRepository productRepo, IOrderRepository orderRepo) {
        return (args) -> {
            logger.info("Creating product mock data");

            Optional<Product> product = Product.ProductFactory.buildProduct("Banana", "B12345", 100.0);
            product.ifPresent(productRepo::save);

            logger.info("Finished product mock data creation");

            if (product.isPresent()) {
                logger.info("Creating order mock data");
                Order.OrderFactory orderFactory = new Order.OrderFactory();
                orderFactory.addProduct(product.get(), 2);

                Optional<Order> order = orderFactory.buildOrder();
                order.ifPresent(orderRepo::save);

                logger.info("Finished order mock data creation");
            }
        };
    }
}
