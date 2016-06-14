package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.09.2015.
 */
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
        if (!isLoggedUser(userId)) {
            return null;
        }

        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        repository.put(userMeal.getId(), userMeal);
        return userMeal;
    }

    @Override
    public void delete(int userId, int mealId) {
        if (!isLoggedUser(userId)) {
            return;
        }
        repository.remove(mealId);
    }

    @Override
    public UserMeal get(int userId, int mealId) {
        if (!isLoggedUser(userId)) {
            return null;
        }
        return repository.get(mealId);
    }

    @Override
    public Collection<UserMeal> getAll(int userId) {
        if (!isLoggedUser(userId)) {
            return null;
        }
        return repository.values();
    }

    private boolean isLoggedUser(int userId) {
        return LoggedUser.id() == userId;
    }

    private UserMeal init(UserMeal userMeal) {
        LOG.info("init for user id=" + LoggedUser.id());
        return save(LoggedUser.id(), userMeal);
    }
}

