// src/test/java/ru/praktikum/tests/orders/OrderCreationTests.java
package ru.praktikum.tests.orders;

import io.qameta.allure.*;
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
    @Story("Создание заказа с авторизацией")
    public void createOrder_authorized_success() {
        var resp = orderClient.createOrder(validIngredientIds, accessToken);
        asserts.assertOrderCreated(resp);
    }

    @Test
    @Story("Создание заказа без авторизации")
    public void createOrder_unauthorized_error() {
        var resp = orderClient.createOrder(validIngredientIds, null);
        asserts.assertOrderUnauthorized(resp);
    }

    @Test
    @Story("Создание заказа с ингредиентами (повторный позитивный сценарий)")
    public void createOrder_withIngredients_again_success() {
        var resp = orderClient.createOrder(validIngredientIds, accessToken);
        asserts.assertOrderCreated(resp);
    }

    @Test
    @Story("Создание заказа без ингредиентов (с авторизацией)")
    public void createOrder_noIngredients_error() {
        var resp = orderClient.createOrder(new ArrayList<>(), accessToken);
        asserts.assertOrderNoIngredients(resp);
    }

    @Test
    @Story("Создание заказа с неверным хешем ингредиента (с авторизацией)")
    public void createOrder_invalidIngredientHash_error() {
        List<String> mixed = new ArrayList<>(validIngredientIds);
        mixed.add(data.invalidIngredientId());
        var resp = orderClient.createOrder(mixed, accessToken);
        asserts.assertOrderInvalidIngredientHash(resp);
    }
}
