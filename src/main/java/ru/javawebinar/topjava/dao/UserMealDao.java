package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.UserMeal;

import java.util.List;

/**
 * Created by Arcanum on 08.06.2016.
 */
public interface UserMealDao {
    void add(UserMeal meal);

    void delete(int id);

    void update(UserMeal meal);

    UserMeal getById(int id);

    List<UserMeal> getAllMeals();
}
