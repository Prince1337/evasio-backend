package evasio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BillingPortalDTO {
    private String id;
    private String url;
    private String returnUrl;
}
