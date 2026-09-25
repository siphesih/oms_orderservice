package fnb.oms_orderservice.service.impl;

import fnb.oms_orderservice.dto.CreateOrderRequest;
import fnb.oms_orderservice.dto.OrderItemRequest;
import fnb.oms_orderservice.dto.OrderItemResponse;
import fnb.oms_orderservice.dto.OrderResponse;
import fnb.oms_orderservice.entity.InventoryItem;
import fnb.oms_orderservice.entity.Order;
import fnb.oms_orderservice.entity.OrderItem;
import fnb.oms_orderservice.entity.OrderStatus;
import fnb.oms_orderservice.exception.InsufficientStockException;
import fnb.oms_orderservice.exception.OrderAccessDeniedException;
import fnb.oms_orderservice.exception.ResourceNotFoundException;
import fnb.oms_orderservice.repository.InventoryItemRepository;
import fnb.oms_orderservice.repository.OrderRepository;
import fnb.oms_orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(Long customerId, CreateOrderRequest request) {

        Order order = Order.builder()
                .customerId(customerId)
                .deliveryNeeded(request.isDeliveryNeeded())
                .status(request.isDeliveryNeeded() ? OrderStatus.PLACED : OrderStatus.PLACED)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            InventoryItem inventoryItem = inventoryItemRepository
                    .findById(itemRequest.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Item not found: " + itemRequest.getItemId()));

            if (inventoryItem.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for: " + inventoryItem.getItemName());
            }

            // Decrement stock — @Version on InventoryItem protects against
            // two orders racing to buy the last few units at the same time
            inventoryItem.setStockQuantity(
                    inventoryItem.getStockQuantity() - itemRequest.getQuantity());
            inventoryItemRepository.save(inventoryItem);

            OrderItem orderItem = OrderItem.builder()
                    .item(inventoryItem)
                    .quantity(itemRequest.getQuantity())
                    .unitPriceAtPurchase(inventoryItem.getPrice())
                    .build();

            order.addOrderItem(orderItem);

            total = total.add(
                    inventoryItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder, "Order placed successfully.");
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Ownership check — a customer can only view THEIR OWN order,
        // even if they guess a valid orderId belonging to someone else
        if (!order.getCustomerId().equals(customerId)) {
            throw new OrderAccessDeniedException("You do not have access to this order.");
        }

        return toResponse(order, null);
    }

    @Override
    public List<OrderResponse> getOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(order -> toResponse(order, null))
                .toList();
    }

    private OrderResponse toResponse(Order order, String message) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(oi -> OrderItemResponse.builder()
                        .itemId(oi.getItem().getItemId())
                        .itemName(oi.getItem().getItemName())
                        .quantity(oi.getQuantity())
                        .unitPriceAtPurchase(oi.getUnitPriceAtPurchase())
                        .subtotal(oi.getSubtotal())
                        .build())
                .toList();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .deliveryNeeded(order.isDeliveryNeeded())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .message(message)
                .build();
    }
}
