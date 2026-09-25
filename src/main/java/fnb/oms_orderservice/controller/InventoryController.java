package fnb.oms_orderservice.controller;

import fnb.oms_orderservice.dto.InventoryItemResponse;
import fnb.oms_orderservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Public — no token required, matches PUBLIC_ROUTES in SecurityConfig
    @GetMapping
    public List<InventoryItemResponse> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public InventoryItemResponse getItemById(@PathVariable Long itemId) {
        return inventoryService.getItemById(itemId);
    }
}
