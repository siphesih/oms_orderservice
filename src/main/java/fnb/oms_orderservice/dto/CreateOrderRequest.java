package fnb.oms_orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    // customerId is NOT taken from the request body — it comes from the JWT token
    private boolean deliveryNeeded;

    @NotEmpty(message = "An order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;
}
