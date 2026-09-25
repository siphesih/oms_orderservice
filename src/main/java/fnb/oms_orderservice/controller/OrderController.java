package fnb.oms_orderservice.controller;

import fnb.oms_orderservice.dto.CreateOrderRequest;
import fnb.oms_orderservice.dto.OrderResponse;
import fnb.oms_orderservice.security.AuthenticatedUser;
import fnb.oms_orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long customerId = getCurrentCustomerId();
        OrderResponse response = orderService.createOrder(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId) {
        Long customerId = getCurrentCustomerId();
        return orderService.getOrderById(orderId, customerId);
    }

    @GetMapping
    public List<OrderResponse> getMyOrders() {
        Long customerId = getCurrentCustomerId();
        return orderService.getOrdersForCustomer(customerId);
    }

    // Pulls the customerId out of the JWT — set earlier by
    // JwtAuthenticationFilter into the SecurityContext. Never trust a
    // customerId sent in the request body or URL.
    private Long getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((AuthenticatedUser) authentication.getPrincipal()).customerId();
    }
}
