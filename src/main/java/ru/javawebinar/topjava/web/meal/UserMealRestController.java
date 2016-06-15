package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Arcanum
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(UserMealRestController.class);

    @Autowired
    private UserMealService service;

    public UserMeal create(UserMeal userMeal) throws NotFoundException {
        userMeal.setId(null);
        LOG.info("create " + userMeal);
        return service.save(LoggedUser.id(), userMeal);
    }

    public void update(UserMeal userMeal) throws NotFoundException {
        LOG.info("update " + userMeal);
        service.update(LoggedUser.id(), userMeal);
    }

    public void delete(int mealId) throws NotFoundException {
        LOG.info("delete " + mealId);
        service.delete(LoggedUser.id(), mealId);
    }

    public UserMeal get(int mealId) throws NotFoundException {
        LOG.info("get " + mealId);
        return service.get(LoggedUser.id(), mealId);
    }


    public Collection<UserMealWithExceed> getFiltered(
            LocalDate beginDate, LocalTime beginTime, LocalDate endDate, LocalTime endTime, int calories) {
        LOG.info("getFiltered " + beginDate + " " + beginTime + " " + endDate + " " + endTime + " " + calories);
        return UserMealsUtil.getWithExceeded(
                service.getFiltered(LoggedUser.id(),
                        beginDate == null ? LocalDate.MIN : beginDate,
                        beginTime == null ? LocalTime.MIN : beginTime,
                        endDate == null ? LocalDate.MAX : endDate,
                        endTime == null ? LocalTime.MAX : endTime), calories);
    }


}
