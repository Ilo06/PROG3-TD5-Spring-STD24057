package ilo.ingredienttd5springstd24057.controller;

import ilo.ingredienttd5springstd24057.entity.*;
import ilo.ingredienttd5springstd24057.service.IngredientService;
import ilo.ingredienttd5springstd24057.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;
    private final StockMovementService stockMovementService;

    public IngredientController(IngredientService ingredientService,
                                StockMovementService stockMovementService) {
        this.ingredientService = ingredientService;
        this.stockMovementService = stockMovementService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.ok(ingredientService.getIngredients());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Integer id) {
        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getIngredientStock(
            @PathVariable Integer id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {

        if (at == null || unit == null) {
            return ResponseEntity.status(400)
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }

        Instant atInstant;
        UnitEnum unitEnum;
        try {
            atInstant = Instant.parse(at);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body("Invalid value for `at` parameter. Expected ISO-8601 format (e.g. 2024-01-06T12:00:00Z).");
        }
        try {
            unitEnum = UnitEnum.valueOf(unit.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body("Invalid value for `unit` parameter. Expected one of: PCS, KG, L.");
        }

        StockValue stockValue = ingredientService.getStockAt(id, atInstant, unitEnum);
        return ResponseEntity.ok(stockValue);
    }

    // f) GET /ingredients/{id}/stockMovements?from={from}&to={to}
    @GetMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<?> getStockMovements(
            @PathVariable Integer id,
            @RequestParam Instant from,
            @RequestParam Instant to) {

        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }

        List<StockMovement> movements = stockMovementService.getStockMovements(id, from, to);
        return ResponseEntity.ok(movements);
    }

    // g) POST /ingredients/{id}/stockMovements
    @PostMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<?> addStockMovements(
            @PathVariable Integer id,
            @RequestBody List<StockMovementCreateRequest> requests) {

        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }

        List<StockMovement> created = stockMovementService.addStockMovements(id, requests);
        return ResponseEntity.ok(created);
    }
}
