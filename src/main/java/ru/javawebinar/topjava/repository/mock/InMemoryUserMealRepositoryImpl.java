package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Arcanum
 * 14.06.2016.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserMealRepositoryImpl.class);
    public static final String NOT_AUTHORIZED = "Not authorized";

    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);


    {
        UserMealsUtil.MEAL_LIST.forEach(this::init);
    }

    @Override
    public UserMeal save(int userId, UserMeal userMeal) {
        if (!isOpertationAllowed(userId, userMeal)) {
            return null;
        }

        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        repository.put(userMeal.getId(), userMeal);
        return userMeal;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        if (!isOpertationAllowed(userId, repository.get(mealId))) {
            return false;
        }
        return repository.remove(mealId) != null;
    }

    @Override
    public UserMeal get(int userId, int mealId) {
        if (!isOpertationAllowed(userId, repository.get(mealId))) {
            return null;
        }
        return repository.get(mealId);
    }

    @Override
    public Collection<UserMeal> getAll(int userId) {
        if (!isLoggedUser(userId)) {
            return null;
        }
        return repository.values().stream().filter(meal -> meal.getUserId().equals(userId))
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<UserMeal> getFiltered(int userId, LocalDate beginDate, LocalTime beginTime, LocalDate endDate,
                                            LocalTime endTime) {
        if (!isLoggedUser(userId)) {
            return null;
        }
        return UserMealsUtil.getFilteredByDateTime(repository.values().stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .collect(Collectors.toList()), beginDate, beginTime, endDate, endTime);
    }

    private boolean isLoggedUser(int userId) {
        return LoggedUser.id() == userId;
    }

    private boolean isLoggedUserMeal(UserMeal meal) {
        if (meal == null) {
            return false;
        }
        return LoggedUser.id() == meal.getUserId();
    }

    private boolean isOpertationAllowed(int userId, UserMeal userMeal) {
        return isLoggedUser(userId) && isLoggedUserMeal(userMeal);
    }

    private UserMeal init(UserMeal userMeal) {
        LOG.debug("init meal=" + userMeal + " + for user id=" + LoggedUser.id());
        return save(LoggedUser.id(), userMeal);
    }
}

