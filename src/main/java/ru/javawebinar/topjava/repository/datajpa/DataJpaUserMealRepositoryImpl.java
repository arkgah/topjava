package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GKislin
 * 27.03.2015.
 */
@Repository
public class DataJpaUserMealRepositoryImpl implements UserMealRepository {
    @Autowired
    ProxyUserMealRepository proxyUserMealRepository;
    @Autowired
    ProxyUserRepository proxyUserRepository;

    private static final Sort SORT_BY_DATE_TIME = new Sort(Sort.Direction.DESC, "dateTime");

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        User user = proxyUserRepository.findOne(userId);
        userMeal.setUser(user);
        return proxyUserMealRepository.save(userMeal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return proxyUserMealRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        return proxyUserMealRepository.findOneByIdAndUserId(id, userId);
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return proxyUserMealRepository.findByUserId(userId, SORT_BY_DATE_TIME);
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return proxyUserMealRepository.findByDateTimeBetweenAndUserId(startDate, endDate, userId, SORT_BY_DATE_TIME);
    }
}
