package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0),
                2000));
    }

    /**
     * Returns a filtered list with the correctly set the 'exceeded' field
     * должны возвращаться только записи между startTime и endTime
     * поле UserMealWithExceed.exceed должно показывать,
     * превышает ли сумма калорий за весь день параметра метода caloriesPerDay
     * Т.е UserMealWithExceed - это запись одной еды, но поле exceeded будет одинаково для всех записей за этот день
     *
     * @param mealList
     * @param startTime
     * @param endTime
     * @param caloriesPerDay
     * @return
     */
    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Integer> mapDate2Calories = new HashMap<>();
        Map<LocalDate, List<UserMeal>> mapDate2MealFilter = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            LocalTime localTime = meal.getDateTime().toLocalTime();
            if (!mapDate2Calories.containsKey(localDate)) {
                mapDate2Calories.put(localDate, meal.getCalories());
            } else {
                mapDate2Calories.put(localDate, mapDate2Calories.get(localDate) + meal.getCalories());
            }

            if (TimeUtil.isBetween(localTime, startTime, endTime)) {
                if (!mapDate2MealFilter.containsKey(localDate)) {
                    mapDate2MealFilter.put(localDate, new ArrayList<>());
                }
                mapDate2MealFilter.get(localDate).add(meal);
            }
        }

        for (LocalDate localDate : mapDate2MealFilter.keySet()) {
            boolean exceeded = false;
            if (mapDate2Calories.get(localDate) > caloriesPerDay) {
                exceeded = true;
            } else {
                exceeded = false;
            }

            for (UserMeal meal : mapDate2MealFilter.get(localDate)) {
                result.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        exceeded));
            }
        }
        return result;
    }


}
