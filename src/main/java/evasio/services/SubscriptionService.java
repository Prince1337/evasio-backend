package evasio.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.param.SubscriptionListParams;
import evasio.dto.SubscriptionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionService {

    public List<SubscriptionDTO> getActiveSubscription(String customerId) throws StripeException {
        SubscriptionListParams params =
                SubscriptionListParams.builder()
                        .setCustomer(customerId)
                        .setStatus(SubscriptionListParams.Status.valueOf("active"))
                        .build();

        SubscriptionCollection subscriptions = Subscription.list(params);
        return subscriptions.getData().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private SubscriptionDTO convertToDTO(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .customer(subscription.getCustomer())
                .status(subscription.getStatus())
                .build();
    }
}
