package ilo.ingredienttd5springstd25057.controller;

import ilo.ingredienttd5springstd25057.entity.Ingredient;
import ilo.ingredienttd5springstd25057.repository.IngredientRepository;
import ilo.ingredienttd5springstd25057.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {
//    private final IngredientRepository ingredientRepository;
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
//        this.ingredientRepository = ingredientRepository;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        try {
            List<Ingredient> ingredients = ingredientService.getIngredients();
            return ResponseEntity.ok(ingredients);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
