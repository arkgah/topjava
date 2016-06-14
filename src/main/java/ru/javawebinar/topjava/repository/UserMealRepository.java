package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.to.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * GKislin
 * 06.03.2015.
 */
public interface UserMealRepository {
    UserMeal save(int userId, UserMeal userMeal);

    boolean delete(int userId, int mealId);

    UserMeal get(int userId, int mealId);

    Collection<UserMeal> getAll(int userId);

    Collection<UserMeal> getFiltered(
            int userId, LocalDate beginDate, LocalTime beginTime, LocalDate endDate, LocalTime endTime
    );
}
