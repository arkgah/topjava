package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.UserMealDao;
import ru.javawebinar.topjava.dao.UserMealInMemoryDao;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.TimeUtil.toLocalDateTime;

/**
 * Created by Arcanum on 06.06.2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private static final String MEAL_LIST_JSP = "/mealList.jsp";
    private static final String MEAL_LIST_JSP_LIST_ATTR = "mealWithExceedList";
    private static final String MEAL_LIST_JSP_ERROR = "error";

    private UserMealDao dao = new UserMealInMemoryDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserMeal> mealList = dao.getAllMeals();
        List<UserMealWithExceed> mealWithExceedList = UserMealsUtil.getFilteredWithExceeded(
                mealList,
                LocalTime.of(0, 0),
                LocalTime.of(23, 59),
                2000);
        request.setAttribute(MEAL_LIST_JSP_LIST_ATTR, mealWithExceedList);
        // Do forward. With the redirect the attributes will be lost
        request.getRequestDispatcher(MEAL_LIST_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // Set the encoding properly, otherwise it will cause the encoding problems with the unicode.
        request.setCharacterEncoding("UTF-8");
        String error = "";
        try {
            UserMeal userMeal = new UserMeal(toLocalDateTime(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories"))
            );
            dao.add(userMeal);
        } catch (DateTimeParseException e ) {
            error = "Illegal time";
        } catch (NumberFormatException e) {
            error = "Illegal calories";
        }

        RequestDispatcher view = request.getRequestDispatcher(MEAL_LIST_JSP);
        request.setAttribute(
                MEAL_LIST_JSP_LIST_ATTR,
                UserMealsUtil.getFilteredWithExceeded(
                        dao.getAllMeals(), LocalTime.of(0, 0),
                        LocalTime.of(23, 59),
                        2000));
        request.setAttribute(MEAL_LIST_JSP_ERROR, error);
        view.forward(request, response);
    }
}
