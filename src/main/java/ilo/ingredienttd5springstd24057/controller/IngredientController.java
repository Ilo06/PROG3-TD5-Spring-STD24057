package ilo.ingredienttd5springstd24057.controller;

import ilo.ingredienttd5springstd24057.entity.Ingredient;
import ilo.ingredienttd5springstd24057.entity.StockValue;
import ilo.ingredienttd5springstd24057.entity.UnitEnum;
import ilo.ingredienttd5springstd24057.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // a) GET /ingredients
    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.ok(ingredientService.getIngredients());
    }

    // b) GET /ingredients/{id}
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Integer id) {
        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }
        return ResponseEntity.ok(ingredient);
    }

    // c) GET /ingredients/{id}/stock?at={temporal}&unit={unit}
    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getIngredientStock(
            @PathVariable Integer id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit) {

        // Validate mandatory query params
        if (at == null || unit == null) {
            return ResponseEntity.status(400)
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        // Validate ingredient exists
        Ingredient ingredient = ingredientService.getIngredientById(id);
        if (ingredient == null) {
            return ResponseEntity.status(404)
                    .body("Ingredient.id=" + id + " is not found");
        }

        // Parse params
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
}
