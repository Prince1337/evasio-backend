package evasio.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import evasio.dto.BillingPortalDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-portal")
public class BillingPortalController {
    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @Value("${stripe.domain}")
    private String domain;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping
        public ResponseEntity<BillingPortalDTO> createCustomerPortalSession(@RequestParam String customerId) throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setCustomer(customerId)
                        .setReturnUrl(domain + "/portal")
                        .build();

        Session session = Session.create(params);
        System.out.println("session: " + session);
        return ResponseEntity.ok(BillingPortalDTO.builder().url(session.getUrl())
                        .id(session.getId())
                        .returnUrl(session.getReturnUrl())
                .build());
    }
}
