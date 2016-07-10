package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Arcanum on 10.07.2016.
 */
public abstract class BaseDbTest {
    private UserService service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        service.evictCache();
    }
}
