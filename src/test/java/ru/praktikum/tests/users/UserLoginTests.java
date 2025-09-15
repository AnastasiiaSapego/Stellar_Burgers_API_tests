package ru.praktikum.tests.users;

import io.qameta.allure.*;
import io.qameta.allure.junit4.AllureJunit4;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.base.BaseTest;
import ru.praktikum.clients.UserClient;
import ru.praktikum.models.Credentials;
import ru.praktikum.models.User;
import ru.praktikum.utils.AssertionsHelper;
import ru.praktikum.utils.DataFactory;

@Epic("Stellar Burgers API")
@Feature("Users: Login")
public class UserLoginTests extends BaseTest {

    private UserClient userClient;
    private AssertionsHelper asserts;
    private DataFactory data;
    private User registeredUser;

    @Before
    public void setup() {
        userClient = new UserClient();
        asserts = new AssertionsHelper();
        data = new DataFactory();

        registeredUser = data.newUser();
        var reg = userClient.register(registeredUser);
        asserts.assertRegistrationSuccess(reg);
        rememberTokens(reg.then().extract().path("accessToken"),
                reg.then().extract().path("refreshToken"));
    }

    @Test
    @Story("Вход под существующим пользователем")
    public void loginExistingUser_success() {
        var resp = userClient.login(new Credentials(
                registeredUser.getEmail(), registeredUser.getPassword()));
        asserts.assertLoginSuccess(resp);
    }

    @Test
    @Story("Вход с неверным паролем")
    public void loginWrongPassword_error() {
        var resp = userClient.login(new Credentials(
                registeredUser.getEmail(), "wrong" + registeredUser.getPassword()));
        asserts.assertLoginUnauthorized(resp);
    }

    @Test
    @Story("Вход с несуществующим email")
    public void loginWrongEmail_error() {
        var resp = userClient.login(new Credentials(
                data.uniqueEmail(), registeredUser.getPassword()));
        asserts.assertLoginUnauthorized(resp);
    }
}