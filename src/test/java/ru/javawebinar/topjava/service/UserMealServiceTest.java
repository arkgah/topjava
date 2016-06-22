package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Arcanum on 21.06.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceTest {

    @Autowired
    protected UserMealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        UserMeal meal = service.get(MEAL_USER_ID, USER_ID);
        MATCHER.assertEquals(meal, MEAL_USER);

        meal = service.get(MEAL_ADMIN_ID, ADMIN_ID);
        MATCHER.assertEquals(meal, MEAL_ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAlien() throws Exception {
        service.get(MEAL_ADMIN_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        service.delete(MEAL_USER_ID, USER_ID);
        service.get(MEAL_USER_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testAlienDelete() throws Exception {
        service.delete(MEAL_ADMIN_ID, USER_ID);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        Collection<UserMeal> meals = service.getBetweenDateTimes(LocalDateTime.MIN, LocalDateTime.MAX, USER_ID);
        Assert.assertEquals(6, meals.size());

        meals = service.getBetweenDateTimes(LocalDateTime.of(2016, 6, 19, 8, 0), LocalDateTime.of(2016, 6, 19, 10, 0),
                USER_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(MEAL_USER), meals);
    }

    public void testGetBetweenAlienDateTimes() throws Exception {
        Collection<UserMeal> meals = service.getBetweenDateTimes(LocalDateTime.MIN, LocalDateTime.MAX, ADMIN_ID);
        Assert.assertEquals(0, meals.size());
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<UserMeal> meals = service.getAll(USER_ID);
        Assert.assertEquals(6, meals.size());

        meals = service.getAll(ADMIN_ID);
        Assert.assertEquals(3, meals.size());
    }

    @Test
    public void testAlienGetAll() throws Exception {
        Collection<UserMeal> meals = service.getAll(1);
        Assert.assertEquals(0, meals.size());
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = new UserMeal();
        updated.setId(MEAL_USER.getId());
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("Замена");
        updated.setCalories(10000);

        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(MEAL_USER_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateAlien() throws Exception {
        UserMeal updated = new UserMeal();
        updated.setId(MEAL_USER.getId());
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("Замена еды");
        updated.setCalories(20000);

        service.update(updated, ADMIN_ID);
    }

    @Test
    public void testSave() throws Exception {
        UserMeal newMeal = new UserMeal(LocalDateTime.now(), "New", 777);
        UserMeal created = service.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        MATCHER.assertEquals(newMeal, created);
    }

}