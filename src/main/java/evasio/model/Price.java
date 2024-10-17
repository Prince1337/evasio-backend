package evasio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

    @Id
    private String id;
    private String object;
    private boolean active;

    @Column(name = "billing_scheme")
    private String billingScheme;

    private long created;
    private String currency;

    @Column(name = "custom_unit_amount")
    private String customUnitAmount;

    private boolean livemode;

    @Column(name = "lookup_key")
    private String lookupKey;

    @ElementCollection
    @CollectionTable(name = "price_metadata", joinColumns = @JoinColumn(name = "price_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata;

    private String nickname;
    private String product;

    @Embedded
    private Recurring recurring;

    @Column(name = "tax_behavior")
    private String taxBehavior;

    @Column(name = "tiers_mode")
    private String tiersMode;

    @Column(name = "transform_quantity")
    private String transformQuantity;

    private String type;

    @Column(name = "unit_amount")
    private int unitAmount;

    @Column(name = "unit_amount_decimal")
    private String unitAmountDecimal;

    // Getters and setters omitted for brevity

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Recurring {
        @Column(name = "aggregate_usage")
        private String aggregateUsage;

        @Column(name = "recurring_interval")
        private String interval;

        @Column(name = "interval_count")
        private int intervalCount;

        private String meter;

        @Column(name = "trial_period_days")
        private String trialPeriodDays;

        @Column(name = "usage_type")
        private String usageType;

        // Getters and setters omitted for brevity
    }
}
