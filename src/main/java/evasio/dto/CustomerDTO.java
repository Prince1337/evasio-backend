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
public class CustomerDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("address")
    private String address;

    @JsonProperty("balance")
    private int balance;

    @JsonProperty("created")
    private long created;

    @JsonProperty("currency")
    private String currency;

    private String defaultSource;

    @JsonProperty("delinquent")
    private boolean delinquent;

    @JsonProperty("description")
    private String description;

    @JsonProperty("discount")
    private String discount;

    @JsonProperty("email")
    private String email;

    private String invoicePrefix;

    private InvoiceSettingsDTO invoiceSettings;

    @JsonProperty("livemode")
    private boolean livemode;

    @JsonProperty("metadata")
    private Map<String, String> metadata;

    @JsonProperty("name")
    private String name;

    private int nextInvoiceSequence;

    @JsonProperty("phone")
    private String phone;

    private List<String> preferredLocales;

    @JsonProperty("shipping")
    private String shipping;

    private String taxExempt;

    private String testClock;

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class InvoiceSettingsDTO {
        private String customFields;

        private String defaultPaymentMethod;

        @JsonProperty("footer")
        private String footer;

        private String renderingOptions;
    }
}
