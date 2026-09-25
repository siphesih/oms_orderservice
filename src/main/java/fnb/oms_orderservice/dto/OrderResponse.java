package fnb.oms_orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private boolean deliveryNeeded;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
    private String message;
}
