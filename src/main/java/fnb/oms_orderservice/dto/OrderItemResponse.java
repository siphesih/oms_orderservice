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
public class OrderItemResponse {

    private Long orderItemId;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPriceAtPurchase;
    private BigDecimal subtotal;
}
