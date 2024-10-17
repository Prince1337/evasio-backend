package evasio.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import evasio.dto.ProductDTO;
import evasio.services.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @Value("${stripe.domain}")
    private String domain;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    private final ProductService productService;


    @GetMapping
    public List<ProductDTO> getAllProducts() throws StripeException {
        System.out.println("getAllProducts called");
        return productService.getAllActiveProducts();
    }

    @GetMapping("/{id}")
    public Optional<ProductDTO> getProduct(@PathVariable String id) throws StripeException {
        System.out.println("getProduct called with id: " + id);
        return productService.getAllActiveProducts().stream().filter(product -> product.getId().equals(id)).findFirst();
    }

}
