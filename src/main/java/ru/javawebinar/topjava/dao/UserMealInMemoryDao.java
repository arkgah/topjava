package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arcanum on 08.06.2016.
 */
public class UserMealInMemoryDao implements UserMealDao {
    private Map<Integer, UserMeal> dataMap;
    private int idCounter;

    public UserMealInMemoryDao() {
        dataMap = new ConcurrentHashMap<>();
        idCounter = 0;
    }

    @Override
    public void add(UserMeal meal) {
        synchronized (dataMap) {
            idCounter++;
            meal.setId(idCounter);
            dataMap.putIfAbsent(idCounter, meal);
        }
    }

    @Override
    public void delete(int id) {
        synchronized (dataMap) {
            dataMap.remove(id);
        }
    }

    @Override
    public void update(UserMeal meal) {
        if (meal.getId() == 0) {
            throw new IllegalArgumentException("meal has not the is set");
        }
        synchronized (dataMap) {
            dataMap.replace(meal.getId(), meal);
        }
    }

    @Override
    public UserMeal getById(int id) {
        return dataMap.getOrDefault(id, new UserMeal(LocalDateTime.now(), "", 0));
    }

    @Override
    public List<UserMeal> getAllMeals() {
        return new ArrayList<>(dataMap.values());
    }
}
