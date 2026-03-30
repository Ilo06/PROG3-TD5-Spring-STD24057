package ilo.ingredienttd5springstd24057.service;

import ilo.ingredienttd5springstd24057.entity.Dish;
import ilo.ingredienttd5springstd24057.entity.Ingredient;
import ilo.ingredienttd5springstd24057.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getDishes() {
        return dishRepository.getDishes();
    }

    public Dish getDishById(Integer id) {
        return dishRepository.getDishById(id);
    }

    public Dish updateDishIngredients(Integer dishId, List<Ingredient> ingredients) {
        return dishRepository.updateDishIngredients(dishId, ingredients);
    }
}
