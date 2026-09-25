package fnb.oms_orderservice.service;

import fnb.oms_orderservice.dto.CreateOrderRequest;
import fnb.oms_orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Long customerId, CreateOrderRequest request);
    OrderResponse getOrderById(Long orderId, Long customerId);
    List<OrderResponse> getOrdersForCustomer(Long customerId);
}
