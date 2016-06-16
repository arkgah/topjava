package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Arcanum
 * 14.06.2016.
 */
@Service
public class UserMealServiceImpl implements UserMealService {
    @Autowired
    private UserMealRepository repository;

    @Override
    public UserMeal save(int userId, UserMeal userMeal) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.save(userId, userMeal), userMeal.getId());
    }

    @Override
    public void delete(int userId, int mealId) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    @Override
    public UserMeal get(int userId, int mealId) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    @Override
    public Collection<UserMealWithExceed> getFilteredWithExceed(
            int userId, LocalDate beginDate, LocalTime beginTime, LocalDate endDate, LocalTime endTime, int
            caloriesPerDay) {
        return ExceptionUtil.checkNotFoundWithId(
                repository.getFilteredWithExceed(userId, beginDate, beginTime, endDate, endTime, caloriesPerDay), userId);
    }

    @Override
    public Collection<UserMeal> getAll(int userId) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.getAll(userId), userId);
    }

    @Override
    public void update(int userId, UserMeal userMeal) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.save(userId, userMeal), userMeal.getId());
    }
}
