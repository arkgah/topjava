package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arcanum on 06.06.2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 3, 10, 3), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 13, 13, 6), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 23, 20, 11), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 22), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        List<UserMealWithExceed> mealWithExceedList = UserMealsUtil.getFilteredWithExceeded(
                mealList,
                LocalTime.of(0, 0),
                LocalTime.of(23, 59),
                2000);
        request.setAttribute("mealWithExceedList", mealWithExceedList);
        // Do forward. With the redirect the attributes will be lost
        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
    }
}
