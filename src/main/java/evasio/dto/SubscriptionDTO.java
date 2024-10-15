package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class SubscriptionDTO {
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("application")
    private String application;

    @JsonProperty("application_fee_percent")
    private Double applicationFeePercent;

    @JsonProperty("automatic_tax")
    private AutomaticTaxDTO automaticTax;

    @JsonProperty("billing_cycle_anchor")
    private long billingCycleAnchor;

    @JsonProperty("billing_thresholds")
    private String billingThresholds;

    @JsonProperty("cancel_at")
    private Long cancelAt;

    @JsonProperty("cancel_at_period_end")
    private boolean cancelAtPeriodEnd;

    @JsonProperty("canceled_at")
    private Long canceledAt;

    @JsonProperty("cancellation_details")
    private CancellationDetailsDTO cancellationDetails;

    @JsonProperty("collection_method")
    private String collectionMethod;

    @JsonProperty("created")
    private long created;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("current_period_end")
    private long currentPeriodEnd;

    @JsonProperty("current_period_start")
    private long currentPeriodStart;

    @JsonProperty("customer")
    private String customer;

    @JsonProperty("days_until_due")
    private Integer daysUntilDue;

    @JsonProperty("default_payment_method")
    private String defaultPaymentMethod;

    @JsonProperty("default_source")
    private String defaultSource;

    @JsonProperty("default_tax_rates")
    private List<String> defaultTaxRates;

    @JsonProperty("description")
    private String description;

    @JsonProperty("discount")
    private String discount;

    @JsonProperty("discounts")
    private String discounts;

    @JsonProperty("ended_at")
    private Long endedAt;

    @JsonProperty("invoice_settings")
    private InvoiceSettingsDTO invoiceSettings;

    @JsonProperty("items")
    private ItemsDTO items;

    @JsonProperty("latest_invoice")
    private String latestInvoice;

    @JsonProperty("livemode")
    private boolean livemode;

    @JsonProperty("metadata")
    private Map<String, String> metadata;

    @JsonProperty("next_pending_invoice_item_invoice")
    private String nextPendingInvoiceItemInvoice;

    @JsonProperty("on_behalf_of")
    private String onBehalfOf;

    @JsonProperty("pause_collection")
    private String pauseCollection;

    @JsonProperty("payment_settings")
    private PaymentSettingsDTO paymentSettings;

    @JsonProperty("pending_invoice_item_interval")
    private String pendingInvoiceItemInterval;

    @JsonProperty("pending_setup_intent")
    private String pendingSetupIntent;

    @JsonProperty("pending_update")
    private String pendingUpdate;

    @JsonProperty("schedule")
    private String schedule;

    @JsonProperty("start_date")
    private long startDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("test_clock")
    private String testClock;

    @JsonProperty("transfer_data")
    private String transferData;

    @JsonProperty("trial_end")
    private Long trialEnd;

    @JsonProperty("trial_settings")
    private TrialSettingsDTO trialSettings;

    @JsonProperty("trial_start")
    private Long trialStart;

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class AutomaticTaxDTO {
        @JsonProperty("enabled")
        private boolean enabled;

        @JsonProperty("liability")
        private String liability;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class CancellationDetailsDTO {
        @JsonProperty("comment")
        private String comment;

        @JsonProperty("feedback")
        private String feedback;

        @JsonProperty("reason")
        private String reason;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class InvoiceSettingsDTO {
        @JsonProperty("issuer")
        private IssuerDTO issuer;

        @Data
        @SuperBuilder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NoArgsConstructor
        public static class IssuerDTO {
            @JsonProperty("type")
            private String type;
        }
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class ItemsDTO {
        @JsonProperty("object")
        private String object;

        @JsonProperty("data")
        private List<SubscriptionItemDTO> data;

        @JsonProperty("has_more")
        private boolean hasMore;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("url")
        private String url;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class SubscriptionItemDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("object")
        private String object;

        @JsonProperty("billing_thresholds")
        private String billingThresholds;

        @JsonProperty("created")
        private long created;

        @JsonProperty("metadata")
        private Map<String, String> metadata;

        @JsonProperty("plan")
        private PlanDTO plan;

        @JsonProperty("price")
        private PriceDTO price;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("subscription")
        private String subscription;

        @JsonProperty("tax_rates")
        private List<String> taxRates;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class PlanDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("object")
        private String object;

        @JsonProperty("active")
        private boolean active;

        @JsonProperty("aggregate_usage")
        private String aggregateUsage;

        @JsonProperty("amount")
        private int amount;

        @JsonProperty("amount_decimal")
        private String amountDecimal;

        @JsonProperty("billing_scheme")
        private String billingScheme;

        @JsonProperty("created")
        private long created;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("discounts")
        private String discounts;

        @JsonProperty("interval")
        private String interval;

        @JsonProperty("interval_count")
        private int intervalCount;

        @JsonProperty("livemode")
        private boolean livemode;

        @JsonProperty("metadata")
        private Map<String, String> metadata;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("product")
        private String product;

        @JsonProperty("tiers_mode")
        private String tiersMode;

        @JsonProperty("transform_usage")
        private String transformUsage;

        @JsonProperty("trial_period_days")
        private Integer trialPeriodDays;

        @JsonProperty("usage_type")
        private String usageType;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class PriceDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("object")
        private String object;

        @JsonProperty("active")
        private boolean active;

        @JsonProperty("billing_scheme")
        private String billingScheme;

        @JsonProperty("created")
        private long created;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("custom_unit_amount")
        private String customUnitAmount;

        @JsonProperty("livemode")
        private boolean livemode;

        @JsonProperty("lookup_key")
        private String lookupKey;

        @JsonProperty("metadata")
        private Map<String, String> metadata;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("product")
        private String product;

        @JsonProperty("recurring")
        private RecurringDTO recurring;

        @JsonProperty("tax_behavior")
        private String taxBehavior;

        @JsonProperty("tiers_mode")
        private String tiersMode;

        @JsonProperty("transform_quantity")
        private String transformQuantity;

        @JsonProperty("type")
        private String type;

        @JsonProperty("unit_amount")
        private int unitAmount;

        @JsonProperty("unit_amount_decimal")
        private String unitAmountDecimal;

        @Data
        @SuperBuilder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NoArgsConstructor
        public static class RecurringDTO {
            @JsonProperty("aggregate_usage")
            private String aggregateUsage;

            @JsonProperty("interval")
            private String interval;

            @JsonProperty("interval_count")
            private int intervalCount;

            @JsonProperty("trial_period_days")
            private Integer trialPeriodDays;

            @JsonProperty("usage_type")
            private String usageType;
        }
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class PaymentSettingsDTO {
        @JsonProperty("payment_method_options")
        private String paymentMethodOptions;

        @JsonProperty("payment_method_types")
        private String paymentMethodTypes;

        @JsonProperty("save_default_payment_method")
        private String saveDefaultPaymentMethod;
    }

    @Data
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    public static class TrialSettingsDTO {
        @JsonProperty("end_behavior")
        private EndBehaviorDTO endBehavior;

        @Data
        @SuperBuilder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @NoArgsConstructor
        public static class EndBehaviorDTO {
            @JsonProperty("missing_payment_method")
            private String missingPaymentMethod;
        }
    }
}
