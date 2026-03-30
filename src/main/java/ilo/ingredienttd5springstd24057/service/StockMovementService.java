package ilo.ingredienttd5springstd24057.service;

import ilo.ingredienttd5springstd24057.entity.StockValue;
import ilo.ingredienttd5springstd24057.entity.UnitEnum;
import ilo.ingredienttd5springstd24057.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public StockValue getStockValueAt(Integer ingredientId, Instant at, UnitEnum unit) {
        return stockMovementRepository.getStockValueAt(ingredientId, at, unit);
    }
}
