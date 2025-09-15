package ru.praktikum.tests.users;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.AllureJunit4;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.base.BaseTest;
import ru.praktikum.clients.UserClient;
import ru.praktikum.models.User;
import ru.praktikum.utils.AssertionsHelper;
import ru.praktikum.utils.DataFactory;

import java.util.HashMap;
import java.util.Map;

@Epic("Stellar Burgers API")
@Feature("Users: Registration")
public class UserRegistrationTests extends BaseTest {

    private UserClient userClient;
    private AssertionsHelper asserts;
    private DataFactory data;

    @Before
    public void setup() {
        userClient = new UserClient();
        asserts = new AssertionsHelper();
        data = new DataFactory();
    }

    @Test
    @Story("Создать уникального пользователя")
    @Description("Позитивный сценарий: регистрация с валидными email/password/name")
    public void registerUniqueUser_success() {
        User user = data.newUser();

        var resp = userClient.register(user);
        asserts.assertRegistrationSuccess(resp);

        String access = resp.then().extract().path("accessToken");
        String refresh = resp.then().extract().path("refreshToken");
        rememberTokens(access, refresh);
    }

    @Test
    @Story("Создать пользователя, который уже зарегистрирован")
    @Description("Негативный сценарий: вторая регистрация с тем же email")
    public void registerAlreadyExists_error() {
        User user = data.newUser();

        var first = userClient.register(user);
        asserts.assertRegistrationSuccess(first);
        rememberTokens(first.then().extract().path("accessToken"),
                first.then().extract().path("refreshToken"));

        var second = userClient.register(user);
        asserts.assertUserAlreadyExists(second);
    }

    @Test
    @Story("Создать пользователя без одного из обязательных полей")
    @Description("Негативный сценарий: пропустить email")
    public void registerMissingRequiredField_error_email() {
        Map<String, Object> body = new HashMap<>(); // без email
        body.put("password", data.validPassword());
        body.put("name", data.validName());

        var resp = userClient.registerRaw(body);
        asserts.assertMissingFieldsOnRegistration(resp);
    }
}

