package evasio.api;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String sigHeader = request.getHeader("Stripe-Signature");
        String endpointSecret = "your_webhook_secret";

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            // Verarbeiten Sie das Ereignis
            if ("checkout.session.completed".equals(event.getType())) {
                // Handle the checkout session completed event
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }
    }
}
