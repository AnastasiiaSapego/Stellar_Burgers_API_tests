package ru.praktikum.tests.orders;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.base.BaseTest;
import ru.praktikum.clients.IngredientClient;
import ru.praktikum.clients.OrderClient;
import ru.praktikum.clients.UserClient;
import ru.praktikum.models.User;
import ru.praktikum.utils.AssertionsHelper;
import ru.praktikum.utils.DataFactory;

import java.util.ArrayList;
import java.util.List;

@Epic("Stellar Burgers API")
@Feature("Orders: Create")
public class OrderCreationTests extends BaseTest {

    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private UserClient userClient;
    private AssertionsHelper asserts;
    private DataFactory data;
    private static final String INVALID_HASH = "InvalidHash123";

    private List<String> validIngredientIds;

    @Before
    public void setup() {
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        userClient = new UserClient();
        asserts = new AssertionsHelper();
        data = new DataFactory();

        List<String> all = ingredientClient.getAllIds();
        validIngredientIds = all.size() >= 2 ? all.subList(0, 2) : all;

        User u = data.newUser();
        var reg = userClient.register(u);
        asserts.assertRegistrationSuccess(reg);
        rememberTokens(reg.then().extract().path("accessToken"),
                reg.then().extract().path("refreshToken"));
    }

    @Test
    @DisplayName("Создать заказ с авторизацией и валидными ингредиентами")
    @Description("Позитив: передаём 2 валидных ингредиента и токен. Ожидаем 200 и номер заказа.")
    public void createOrderAuthorizedSuccess() {
        var resp = orderClient.createOrder(validIngredientIds, accessToken);
        asserts.assertOrderCreated(resp);
    }

    @Test
    @DisplayName("Создать заказ без авторизации -> 200")
    @Description("Позитив: передаём валидные ингредиенты без токена. Ожидаем 200 и номер заказа.")
    public void createOrderUnauthorizedSuccess() {
        var resp = orderClient.createOrder(validIngredientIds, null);
        asserts.assertOrderUnauthorized(resp);
    }

    @Test
    @DisplayName("Повторное создание заказа с авторизацией")
    @Description("Позитив: повторяем успешный сценарий с теми же валидными ингредиентами.")
    public void createOrderWithIngredientsAgainSuccess() {
        var resp = orderClient.createOrder(validIngredientIds, accessToken);
        asserts.assertOrderCreated(resp);
    }

    @Test
    @DisplayName("Создать заказ без ингредиентов -> 400")
    @Description("Негатив: отправляем пустой список ингредиентов, но с токеном. Ожидаем 400 Bad Request.")
    public void createOrderNoIngredientsError() {
        var resp = orderClient.createOrder(new ArrayList<>(), accessToken);
        asserts.assertOrderNoIngredients(resp);
    }

    @Test
    @DisplayName("Создать заказ с одним невалидным хэшем ингредиента -> 500")
    @Description("Негатив: 1 из ингредиентов — рандомный id. Ожидаем 500")
    public void createOrderInvalidIngredientHashError() {
        List<String> mixed = new ArrayList<>(validIngredientIds);
        mixed.add(INVALID_HASH);
        var resp = orderClient.createOrder(mixed, accessToken);
        asserts.assertOrderInvalidIngredientHash(resp);
    }
}