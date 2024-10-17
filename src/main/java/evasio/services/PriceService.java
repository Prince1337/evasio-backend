package evasio.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceUpdateParams;
import evasio.dto.PriceDTO;
import evasio.repositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    // Method to synchronize products and prices with Stripe
    public void synchronizeWithStripe() throws StripeException {

        PriceListParams params = PriceListParams.builder().build();
        PriceCollection prices = Price.list(params);
        List<PriceDTO> localPrices = pricesToDTO(prices);

        for (PriceDTO localPrice : localPrices) {
            com.stripe.model.Price stripePrice = getPriceFromStripe(localPrice.getProduct());
            if (stripePrice != null) {
                updateOrCreatePrice(localPrice, stripePrice);
            }
        }
    }

    private List<PriceDTO> pricesToDTO(PriceCollection prices) {
        return prices
                .getData()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PriceDTO convertToDTO(Price price) {
        return PriceDTO.builder()
                .id(price.getId())
                .active(price.getActive())
                .unitAmount(Math.toIntExact(price.getUnitAmount()))
                .currency(price.getCurrency())
                .product(price.getProduct())
                .build();
    }

    // Method to retrieve price from Stripe
    private com.stripe.model.Price getPriceFromStripe(String productId) throws StripeException {
        PriceListParams params = PriceListParams.builder().setProduct(productId).setActive(true).build();
        List<com.stripe.model.Price> prices = Price.list(params).getData();
        if (!prices.isEmpty()) {
            // Assuming each product has exactly one active price
            return prices.get(0);
        }
        return null;
    }

    // Method to update or create price based on comparison with Stripe
    private void updateOrCreatePrice(PriceDTO localPrice, com.stripe.model.Price stripePrice) throws StripeException {
        long localUnitAmount = localPrice.getUnitAmount();
        long stripeUnitAmount = stripePrice.getUnitAmount();

        // Check if unit amounts match
        if (localUnitAmount != stripeUnitAmount) {
            // If not, create a new price
            createPrice(localPrice.getProduct(), localUnitAmount, localPrice.getCurrency());
            // Then update the existing price to inactive
            stripePrice.update(PriceUpdateParams.builder().setActive(false).build());
        }
    }

    // Method to create price in Stripe
    private void createPrice(String productId, long unitAmount, String currency) throws StripeException {
        PriceCreateParams params = PriceCreateParams.builder()
                .setProduct(productId)
                .setUnitAmount(unitAmount)
                .setCurrency(currency)
                .build();
        Price.create(params);
    }

    public Optional<PriceDTO> findById(String priceId) {
        Optional<evasio.model.Price> price = priceRepository.findById(priceId);

        if (price.isEmpty()) {
            return Optional.empty();
        }
        return convertEntityToDTO(price);
    }

    private Optional<PriceDTO> convertEntityToDTO(Optional<evasio.model.Price> price) {
        if (price.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PriceDTO.builder()
                .id(price.get().getId())
                .active(price.get().isActive())
                .unitAmount(Math.toIntExact(price.get().getUnitAmount()))
                .currency(price.get().getCurrency())
                .product(price.get().getProduct())
                .build());
    }

    public PriceDTO save(PriceDTO priceDTO) {
        evasio.model.Price price = priceRepository.save(evasio.model.Price.builder()
                .id(priceDTO.getId())
                .active(priceDTO.isActive())
                .unitAmount(priceDTO.getUnitAmount())
                .currency(priceDTO.getCurrency())
                .product(priceDTO.getProduct())
                .build());
        return convertEntityToDTO(Optional.of(price)).get();
    }

    // Other methods remain unchanged...

}
