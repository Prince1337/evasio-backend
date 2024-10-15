package evasio.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceListParams;
import com.stripe.param.ProductListParams;
import evasio.dto.PriceDTO;
import evasio.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    public List<ProductDTO> getAllActiveProducts() throws StripeException {
        ProductListParams params = ProductListParams.builder()
                .setActive(true)
                .build();

        List<Product> products = Product.list(params).getData();

        return products.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToProductDTO(Product product) {
        List<Price> prices = getActivePricesForProduct(product.getId());

        return ProductDTO.builder()
                .id(product.getId())
                .object(product.getObject())
                .active(product.getActive())
                .created(product.getCreated())
                .description(product.getDescription())
                .images(product.getImages())
                .features(product.getMetadata().get("features")) // assuming features are stored in metadata
                .livemode(product.getLivemode())
                .metadata(product.getMetadata())
                .name(product.getName())
                .shippable(product.getShippable())
                .statementDescriptor(product.getStatementDescriptor())
                .taxCode(product.getTaxCode())
                .unitLabel(product.getUnitLabel())
                .updated(product.getUpdated())
                .url(product.getUrl())
                .prices(prices.stream().map(this::convertToPriceDTO).collect(Collectors.toList()))
                .build();
    }

    private List<Price> getActivePricesForProduct(String productId) {
        PriceListParams params = PriceListParams.builder()
                .setProduct(productId)
                .setActive(true)
                .build();

        try {
            return Price.list(params).getData();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to fetch prices for product: " + productId, e);
        }
    }

    private PriceDTO convertToPriceDTO(Price price) {
        return PriceDTO.builder()
                .id(price.getId())
                .object(price.getObject())
                .active(price.getActive())
                .billingScheme(price.getBillingScheme())
                .created(price.getCreated())
                .currency(price.getCurrency())
                .livemode(price.getLivemode())
                .lookupKey(price.getLookupKey())
                .metadata(price.getMetadata())
                .nickname(price.getNickname())
                .product(price.getProduct())
                .recurring(price.getRecurring() != null ? PriceDTO.RecurringDTO.builder()
                        .aggregateUsage(price.getRecurring().getAggregateUsage())
                        .interval(price.getRecurring().getInterval())
                        .intervalCount(Math.toIntExact(price.getRecurring().getIntervalCount()))
                        .trialPeriodDays(String.valueOf(price.getRecurring().getTrialPeriodDays()))
                        .usageType(price.getRecurring().getUsageType())
                        .build() : null)
                .taxBehavior(price.getTaxBehavior())
                .tiersMode(price.getTiersMode())
                .transformQuantity(String.valueOf(price.getTransformQuantity()))
                .type(price.getType())
                .unitAmount(Math.toIntExact(price.getUnitAmount()))
                .unitAmountDecimal(String.valueOf(price.getUnitAmountDecimal()))
                .build();
    }
}


