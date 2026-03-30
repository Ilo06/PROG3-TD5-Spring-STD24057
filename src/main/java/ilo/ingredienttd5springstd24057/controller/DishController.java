package ilo.ingredienttd5springstd24057.controller;

import ilo.ingredienttd5springstd24057.entity.Dish;
import ilo.ingredienttd5springstd24057.entity.Ingredient;
import ilo.ingredienttd5springstd24057.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    // d) GET /dishes
    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getDishes() {
        return ResponseEntity.ok(dishService.getDishes());
    }

    // e) PUT /dishes/{id}/ingredients
    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(
            @PathVariable Integer id,
            @RequestBody(required = false) List<Ingredient> ingredients) {

        // Request body is mandatory
        if (ingredients == null) {
            return ResponseEntity.status(400)
                    .body("Request body is required and must contain a list of ingredients.");
        }

        // Check dish exists
        Dish dish = dishService.getDishById(id);
        if (dish == null) {
            return ResponseEntity.status(404)
                    .body("Dish.id=" + id + " is not found");
        }

        Dish updated = dishService.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok(updated);
    }
}
