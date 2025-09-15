package ru.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static ru.praktikum.config.TestConfig.ORDERS;
import ru.praktikum.config.TestConfig;

public class OrderClient {

    @Step("POST /api/orders: создать заказ")
    public Response createOrder(List<String> ingredientIds, String accessToken) {
        List<String> safeIds = (ingredientIds == null) ? Collections.emptyList() : ingredientIds;
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", safeIds);

        var req = given()
                .spec(TestConfig.getBaseRequestSpec())
                .contentType(JSON)
                .body(body);

        if (accessToken != null && !accessToken.isEmpty()) {
            req.header("Authorization", accessToken);
        }

        return req.when().post(ORDERS);
    }

    @Step("POST /api/orders: создать заказ без ингредиентов")
    public Response createOrderWithoutIngredients(String accessToken) {
        return createOrder(Collections.emptyList(), accessToken);
    }
}
