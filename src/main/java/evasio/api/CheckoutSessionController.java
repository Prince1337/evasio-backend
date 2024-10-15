package evasio.api;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import evasio.dto.*;
import evasio.services.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout-sessions")
@RequiredArgsConstructor
public class CheckoutSessionController {

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final PriceService priceService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @Value("${stripe.domain}")
    private String domain;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping
    public ResponseEntity<CheckoutSessionDTO> createCheckoutSession(@RequestBody CheckoutSessionDTO checkoutSessionDTO, @RequestParam String priceId, @RequestParam String mode) {
        System.out.println("priceId: " + priceId);
        System.out.println("checkoutSessionDTO: " + checkoutSessionDTO);

        // Überprüfen Sie die E-Mail des Kunden
        String customerEmail = checkoutSessionDTO.getCustomerEmail();
        System.out.println("Customer Email: " + customerEmail);
        String customerId = userDetailsServiceImplementation.getCustomerId(customerEmail);
        System.out.println("Customer ID: " + customerId);

        if (customerId == null) {
            System.out.println("Error: Customer ID is null");
            return ResponseEntity.badRequest().body(null);
        }

        // Map DTO to Entity
        SessionCreateParams.Mode sessionMode = mode.equals("subscription") ? SessionCreateParams.Mode.SUBSCRIPTION : SessionCreateParams.Mode.PAYMENT;
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl(checkoutSessionDTO.getSuccessUrl())
                .setCancelUrl(checkoutSessionDTO.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceId)
                                .setQuantity(1L)
                                .build()
                )
                .setMode(sessionMode)
                .setCustomer(customerId)
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        System.out.println("session: " + session);

        if (session == null) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(convertFromStripeToDTO(session), HttpStatus.CREATED);
    }

    private CheckoutSessionDTO convertFromStripeToDTO(Session session) {
        return CheckoutSessionDTO.builder()
                .id(session.getId())
                .url(session.getUrl())
                .status(session.getStatus())
                .customerEmail(session.getCustomerEmail())
                .build();
    }

}
