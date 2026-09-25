package fnb.oms_orderservice.service;

import fnb.oms_orderservice.dto.InventoryItemResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryItemResponse> getAllItems();
    InventoryItemResponse getItemById(Long itemId);
}
