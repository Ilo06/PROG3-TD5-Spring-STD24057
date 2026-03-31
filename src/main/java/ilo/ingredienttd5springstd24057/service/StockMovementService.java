package ilo.ingredienttd5springstd24057.service;

import ilo.ingredienttd5springstd24057.entity.*;
import ilo.ingredienttd5springstd24057.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant at, UnitEnum unit) {
        return stockMovementRepository.getStockValueAt(ingredientId, at, unit);
    }

    public List<StockMovement> getStockMovements(Integer ingredientId, Instant from, Instant to) {
        return stockMovementRepository.getStockMovementsByIngredientIdAndDateRange(ingredientId, from, to);
    }

    public List<StockMovement> addStockMovements(Integer ingredientId,
                                                  List<StockMovementCreateRequest> requests) {
        return stockMovementRepository.saveStockMovements(ingredientId, requests);
    }
}
