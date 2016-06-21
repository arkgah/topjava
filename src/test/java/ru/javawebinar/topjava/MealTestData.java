package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {
    public static final int MEAL_USER_ID = START_SEQ * 2;
    public static final int MEAL_ADMIN_ID = START_SEQ * 3;

    public static final UserMeal MEAL_USER = new UserMeal(
            MEAL_USER_ID, LocalDateTime.of(2016, 6, 19, 9, 0, 15),
            "Каша",
            150);

    public static final UserMeal MEAL_ADMIN = new UserMeal(
            MEAL_ADMIN_ID, LocalDateTime.of(2016, 6, 20, 9, 10, 0),
            "Завтрак",
            500);


    public static final ModelMatcher<UserMeal, String> MATCHER = new ModelMatcher<>(UserMeal::toString);

}
