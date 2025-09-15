package ru.praktikum.base;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import ru.praktikum.clients.UserClient;
import ru.praktikum.config.TestConfig;
import ru.praktikum.utils.AllureFilters;

public class BaseTest {
    protected String accessToken;
    protected String refreshToken;
    protected boolean userCreated;

    @BeforeClass
    public static void setUpClass() {
        RestAssured.requestSpecification = TestConfig.getBaseRequestSpec();
        RestAssured.filters(AllureFilters.withAllure());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    public void setUp() {
        accessToken = null;
        refreshToken = null;
        userCreated = false;
    }

    @After
    public void tearDown() {
        if (userCreated && accessToken != null) {
            deleteCurrentUserIfExists();
        }
    }

    @Step("Удалить текущего пользователя, если он был создан")
    protected void deleteCurrentUserIfExists() {
        try {
            new UserClient().deleteUser(accessToken);
        } catch (Exception ignored) {
        }
    }

    protected void rememberTokens(String access, String refresh) {
        this.accessToken = access;
        this.refreshToken = refresh;
        this.userCreated = true;
    }
}

