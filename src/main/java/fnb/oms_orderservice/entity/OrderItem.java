package fnb.oms_orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Price captured AT THE TIME of purchase — if InventoryItem's price
    // changes later, past orders must still show what the customer actually paid
    @Column(name = "unit_price_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceAtPurchase;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (unitPriceAtPurchase != null && quantity != null) {
            this.subtotal = unitPriceAtPurchase.multiply(BigDecimal.valueOf(quantity));
        }
    }
}