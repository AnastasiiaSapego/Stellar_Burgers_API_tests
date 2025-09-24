package ru.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.config.TestConfig;
import ru.praktikum.models.OrderRequest;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static ru.praktikum.config.Endpoints.*;

public class OrderClient {

    @Step("POST {ORDERS}: создать заказ (ingredients={ingredientIds.size()}, auth={accessToken != null})")
    public Response createOrder(List<String> ingredientIds, String accessToken) {
        List<String> safe = (ingredientIds == null) ? Collections.emptyList() : ingredientIds;
        OrderRequest body = new OrderRequest(safe);

        var spec = given()
                .spec(TestConfig.getBaseRequestSpec())
                .contentType(JSON)
                .body(body);

        if (accessToken != null && !accessToken.isEmpty()) {
            spec.header(AUTHORIZATION, accessToken);
        }

        return spec.when().post(ORDERS);
    }

    @Step("POST {ORDERS}: создать заказ без ингредиентов")
    public Response createOrderWithoutIngredients(String accessToken) {
        return createOrder(Collections.emptyList(), accessToken);
    }
}
