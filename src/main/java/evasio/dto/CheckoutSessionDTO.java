package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
public class CheckoutSessionDTO {
    private String id;
    private String object;
    private String afterExpiration;
    private Boolean allowPromotionCodes;
    private Integer amountSubtotal;
    private Integer amountTotal;
    private Boolean automaticTaxEnabled;
    private Boolean billingAddressCollectionEnabled;
    private String cancelUrl;
    private String clientReferenceId;
    private String consent;
    private Boolean consentCollectionEnabled;
    private Long created;
    private String currency;
    private String customerCreation;
    private String customerEmail;
    private Long expiresAt;
    private Boolean livemode;
    private String locale;
    private String mode;
    private String paymentStatus;
    private Boolean phoneNumberCollectionEnabled;
    private String status;
    private String successUrl;
    private String url;

//    @JsonProperty("automatic_tax")
//    private void setAutomaticTax(Map<String, Object> automaticTax) {
//        this.automaticTaxEnabled = (Boolean) automaticTax.get("enabled");
//    }
//
//    @JsonProperty("billing_address_collection")
//    private void setBillingAddressCollection(Map<String, Object> billingAddressCollection) {
//        this.billingAddressCollectionEnabled = (Boolean) billingAddressCollection.get("enabled");
//    }
//
//    @JsonProperty("consent_collection")
//    private void setConsentCollection(Map<String, Object> consentCollection) {
//        this.consentCollectionEnabled = (Boolean) consentCollection.get("enabled");
//    }
//
//    @JsonProperty("phone_number_collection")
//    private void setPhoneNumberCollection(Map<String, Object> phoneNumberCollection) {
//        this.phoneNumberCollectionEnabled = (Boolean) phoneNumberCollection.get("enabled");
//    }
}
