package br.com.douglasffilho.product;

import br.com.douglasffilho.authorizationservice.AuthorizationServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
        ProductApplication.class,
        AuthorizationServiceApplication.class
})
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
