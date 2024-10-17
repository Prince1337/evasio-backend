package evasio.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import evasio.dto.SubscriptionDTO;
import evasio.services.SubscriptionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.stripe.Stripe.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @Value("${stripe.domain}")
    private String domain;

    @PostConstruct
    public void init() {
        apiKey = stripeApiKey;
    }

    @GetMapping
    public List<SubscriptionDTO> getActiveSubscriptions(String customerId) throws StripeException {
        System.out.println("getActiveSubscriptions called with customerId: " + customerId);

        return subscriptionService.getActiveSubscription(customerId);
    }


    private SubscriptionDTO convertFromStripeToDTO(Session session) {
        return SubscriptionDTO.builder()
                .id(session.getId())
                .customer(session.getCustomer())
                .status(session.getStatus())
                .build();
    }
}
