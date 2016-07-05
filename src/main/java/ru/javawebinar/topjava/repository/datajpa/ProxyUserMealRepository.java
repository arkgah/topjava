package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Arcanum on 03.07.2016.
 */
@Transactional(readOnly = true)
public interface ProxyUserMealRepository extends JpaRepository<UserMeal, Integer> {

    @Override
    @Transactional
    UserMeal save(UserMeal meal);

    UserMeal findOneByIdAndUserId(int id, int userId);

    List<UserMeal> findByUserId(int userId, Sort sort);

    List<UserMeal> findByDateTimeBetweenAndUserId(LocalDateTime begin, LocalDateTime end, int userId, Sort
            sort);

    @Transactional
    @Modifying
    int deleteByIdAndUserId(int id, int userId);

}
