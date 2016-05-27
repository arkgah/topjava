package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        // Count the calories per a day basis
        Map<LocalDate, Integer> mapCaloriesByDate = mealList
                .stream()
                .map(e -> pair(e.getDateTime().toLocalDate(), e.getCalories()))
                .collect(Collectors.groupingBy(
                        java.util.Map.Entry::getKey,
                        Collectors.summingInt(java.util.Map.Entry::getValue)));

        List<UserMealWithExceed> result = mealList
                .stream()
                .filter(e -> TimeUtil.isBetween(e.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> new UserMealWithExceed(e.getDateTime(), e.getDescription(), e.getCalories(),
                        mapCaloriesByDate.get(e.getDateTime().toLocalDate()) > caloriesPerDay ? true : false))
                .collect(Collectors.toList());

        return result;
    }

    private static <K, V> AbstractMap.Entry<K, V> pair(K k, V v) {
        return new AbstractMap.SimpleImmutableEntry<K, V>(k, v);
    }


}
