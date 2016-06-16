package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * GKislin
 * 15.06.2015.
 */
public interface UserMealService {
    UserMeal save(int userId, UserMeal userMeal) throws NotFoundException;

    void delete(int userId, int mealId) throws NotFoundException;

    UserMeal get(int userId, int mealId) throws NotFoundException;

    Collection<UserMealWithExceed> getFilteredWithExceed(
            int userId, LocalDate beginDate, LocalTime beginTime, LocalDate endDate, LocalTime endTime, int
            caloriesPerDay);

    Collection<UserMeal> getAll(int userId) throws NotFoundException;

    void update(int userId, UserMeal userMeal) throws NotFoundException;
}
