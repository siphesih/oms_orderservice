package fnb.oms_orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemResponse {

    private Long itemId;
    private String itemName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
}
