package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.meal.UserMealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private UserMealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(UserMealRestController.class);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LOG.info("id=" + id);
        if (id != null) {
            // meal editing logic
            UserMeal userMeal = new UserMeal(LoggedUser.id(), id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));
            LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
            if (userMeal.isNew()) {
                controller.create(userMeal);
            } else {
                controller.update(userMeal);
            }
            response.sendRedirect("meals");
        } else {
            // filter logic
            String beginDateStr = request.getParameter("beginDate");
            LOG.info("beginDate=" + beginDateStr);
            String beginTimeStr = request.getParameter("beginTime");
            LOG.info("beginTime=" + beginTimeStr);
            String endDateStr = request.getParameter("endDate");
            LOG.info("endDate=" + endDateStr);
            String endTimeStr = request.getParameter("endTime");
            LOG.info("endTime=" + endTimeStr);

            try {
                LocalDate beginDate = beginDateStr.isEmpty() ? LocalDate.MIN :
                        LocalDate.parse(beginDateStr, TimeUtil.DATE_FORMATTER);
                LocalTime beginTime = beginTimeStr.isEmpty() ? LocalTime.MIN :
                        LocalTime.parse(beginTimeStr, TimeUtil.TIME_FORMATTER);

                LocalDate endDate = endDateStr.isEmpty() ? LocalDate.MAX :
                        LocalDate.parse(endDateStr, TimeUtil.DATE_FORMATTER);

                LocalTime endTime = endTimeStr.isEmpty() ? LocalTime.MAX :
                        LocalTime.parse(endTimeStr, TimeUtil.TIME_FORMATTER);
                request.setAttribute("mealList",
                        controller.getFiltered(beginDate, beginTime, endDate, endTime, UserMealsUtil
                                .DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/mealList.jsp").forward(request, response);

            } catch (Exception e) {
                LOG.error(e.toString());
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    controller.getFiltered(null, null, null, null, UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            controller.delete(id);
            response.sendRedirect("meals");
        } else if (action.equals("create") || action.equals("update")) {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LoggedUser.id(), LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                    controller.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
