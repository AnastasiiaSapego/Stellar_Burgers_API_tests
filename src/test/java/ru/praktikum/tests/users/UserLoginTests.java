package ru.praktikum.tests.users;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
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
    @DisplayName("Логин с валидными учётными данными")
    @Description("Позитив: корректные email + password -> 200 и токены")
    public void loginExistingUserSuccess() {
        var resp = userClient.login(new Credentials(
                registeredUser.getEmail(), registeredUser.getPassword()));
        asserts.assertLoginSuccess(resp);
    }

    @Test
    @DisplayName("Логин c неверным паролем -> 401")
    @Description("Негатив: правильный email, но изменённый пароль. Ожидаем 401 Unauthorized.")
    public void loginWrongPasswordError() {
        var resp = userClient.login(new Credentials(
                registeredUser.getEmail(), "wrong" + registeredUser.getPassword()));
        asserts.assertLoginUnauthorized(resp);
    }

    @Test
    @DisplayName("Логин c несуществующим email -> 401")
    @Description("Негатив: случайный email, валидный пароль. Ожидаем 401 Unauthorized.")
    public void loginWrongEmailError() {
        var resp = userClient.login(new Credentials(
                data.uniqueEmail(), registeredUser.getPassword()));
        asserts.assertLoginUnauthorized(resp);
    }
}