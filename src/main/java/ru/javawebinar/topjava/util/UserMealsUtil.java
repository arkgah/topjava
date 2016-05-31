package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Arcanum
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
     * @param mealList a list of UserMeal to convert
     * @param startTime the start time of a period to filter
     * @param endTime the end time of a period to filter
     * @param caloriesPerDay calories per day to calculate the exceeded flag
     * @return the list of UserMealWithExceed, filtered by time
     */
    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // Count the calories per a day basis, then derive the exceeded flag
        Map<LocalDate, Boolean> mapCaloriesByDate = mealList
                .stream()
                .collect(Collectors.groupingBy(
                        userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.collectingAndThen(
                                Collectors.summingInt(UserMeal::getCalories),
                                sum -> sum > caloriesPerDay ? true : false
                        )
                        )
                );
        // Then filter by the time intervals with following setting of he flag
        List<UserMealWithExceed> result = mealList
                .stream()
                .filter(e -> TimeUtil.isBetween(e.getDateTime().toLocalTime(), startTime, endTime))
                .map(e -> new UserMealWithExceed(e.getDateTime(), e.getDescription(), e.getCalories(),
                        mapCaloriesByDate.get(e.getDateTime().toLocalDate())))
                .collect(Collectors.toList());

        return result;
    }

}
