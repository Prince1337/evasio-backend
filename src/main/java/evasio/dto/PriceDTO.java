package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PriceDTO {
    private String id;
    private String object;
    private boolean active;

    private String billingScheme;

    private long created;
    private String currency;

    private String customUnitAmount;

    private boolean livemode;

    private String lookupKey;

    private Map<String, String> metadata;
    private String nickname;
    private String product;
    private RecurringDTO recurring;

    private String taxBehavior;

    private String tiersMode;

    private String transformQuantity;

    private String type;

    private int unitAmount;

    private String unitAmountDecimal;

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class RecurringDTO {
        @JsonProperty("aggregate_usage")
        private String aggregateUsage;

        @JsonProperty("interval")
        private String interval;

        private int intervalCount;

        private String trialPeriodDays;

        private String usageType;
    }
}
