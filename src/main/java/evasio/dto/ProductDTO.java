package evasio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ProductDTO {

    private String id;
    private String object;
    private boolean active;
    private long created;
    private String description;
    private List<String> images;
    private boolean livemode;
    private String features;
    private Map<String, String> metadata;
    private String name;
    private Boolean shippable;
    private String statementDescriptor;
    private String taxCode;
    private String unitLabel;
    private long updated;
    private String url;
    private List<PriceDTO> prices;
}
