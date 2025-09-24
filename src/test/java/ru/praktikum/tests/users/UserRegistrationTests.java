package ru.praktikum.tests.users;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.base.BaseTest;
import ru.praktikum.clients.UserClient;
import ru.praktikum.models.User;
import ru.praktikum.utils.AssertionsHelper;
import ru.praktikum.utils.DataFactory;

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
    @DisplayName("Успешная регистрация уникального пользователя")
    @Description("Позитив: валидные email/password/name -> 200 и токены")
    public void registerUniqueUserSuccess() {
        User user = data.newUser();

        var resp = userClient.register(user);
        asserts.assertRegistrationSuccess(resp);

        String access = resp.then().extract().path("accessToken");
        String refresh = resp.then().extract().path("refreshToken");
        rememberTokens(access, refresh);
    }

    @Test
    @DisplayName("Повторная регистрация с тем же email -> ошибка")
    @Description("Негатив: дважды регистрируем одного и того же пользователя")
    public void registerAlreadyExistsError() {
        User user = data.newUser();

        var first = userClient.register(user);
        asserts.assertRegistrationSuccess(first);
        rememberTokens(first.then().extract().path("accessToken"),
                first.then().extract().path("refreshToken"));

        var second = userClient.register(user);
        asserts.assertUserAlreadyExists(second);
    }

    @Test
    @DisplayName("Регистрация без email -> ошибка")
    @Description("Негатив: пропускаем email (email = null)")
    public void registerMissingRequiredFieldErrorEmail() {
        User withoutEmail = new User(null, data.validPassword(), data.validName());
        var resp = userClient.register(withoutEmail);
        asserts.assertMissingFieldsOnRegistration(resp);
    }

    @Test
    @DisplayName("Регистрация без имени -> ошибка")
    @Description("Негатив: пропускаем имя (name = null)")
    public void registerMissingRequiredFieldErrorName() {
        User withoutName = new User(data.uniqueEmail(), data.validPassword(), null);
        var resp = userClient.register(withoutName);
        asserts.assertMissingFieldsOnRegistration(resp);
    }

    @Test
    @DisplayName("Регистрация без пароля -> ошибка")
    @Description("Негатив: пропускаем пароль (password = null)")
    public void registerMissingRequiredFieldErrorPassword() {
        User withoutPassword = new User(data.uniqueEmail(), null, data.validName());
        var resp = userClient.register(withoutPassword);
        asserts.assertMissingFieldsOnRegistration(resp);
    }
}


