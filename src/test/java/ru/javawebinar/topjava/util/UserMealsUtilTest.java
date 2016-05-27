package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by Arcanum on 27.05.2016.
 */
public class UserMealsUtilTest {
    private List<UserMeal> mealList;

    @org.junit.Before
    public void setUp() throws Exception {
        mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void getFilteredMealsWithExceeded() throws Exception {
        List<UserMealWithExceed> res = UserMealsUtil.getFilteredMealsWithExceeded(
                mealList, LocalTime.of(7, 15), LocalTime.of(21, 0), 2000
        );

        assertEquals(mealList.size(), res.size());

        int exceededCount = res.stream().map(e -> e.isExceed()).mapToInt(e -> e ? 1 : 0).sum();
        assertEquals(3, exceededCount);

        assertTrue(res.stream()
                .filter(e -> e.getDateTime().getDayOfMonth() == 30).allMatch(e -> e.isExceed() == false));

        assertTrue(res.stream()
                .filter(e -> e.getDateTime().getDayOfMonth() == 31).allMatch(e -> e.isExceed() == true));

        res = UserMealsUtil.getFilteredMealsWithExceeded(
            mealList, LocalTime.of(0, 0), LocalTime.of(3,0), 2000
        );

        assertEquals(0, res.size());

    }

}