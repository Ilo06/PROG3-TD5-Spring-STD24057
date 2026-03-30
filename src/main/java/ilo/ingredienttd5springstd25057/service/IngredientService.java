package ilo.ingredienttd5springstd25057.service;

import ilo.ingredienttd5springstd25057.entity.Ingredient;
import ilo.ingredienttd5springstd25057.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.getIngredients();
    }
}
