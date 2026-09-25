package fnb.oms_orderservice.service.impl;

import fnb.oms_orderservice.dto.InventoryItemResponse;
import fnb.oms_orderservice.entity.InventoryItem;
import fnb.oms_orderservice.exception.ResourceNotFoundException;
import fnb.oms_orderservice.repository.InventoryItemRepository;
import fnb.oms_orderservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public List<InventoryItemResponse> getAllItems() {
        return inventoryItemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InventoryItemResponse getItemById(Long itemId) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found."));
        return toResponse(item);
    }

    private InventoryItemResponse toResponse(InventoryItem item) {
        return InventoryItemResponse.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .build();
    }
}
