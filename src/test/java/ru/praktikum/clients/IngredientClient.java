package ru.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.apache.http.HttpStatus.*;


public class IngredientClient {

    @Step("GET /api/ingredients: получить список валидных id ингредиентов")
    public List<String> getAllIds() {
        Response resp = given()
                .when()
                .get("/api/ingredients");

        resp.then()
                .statusCode(SC_OK)
                .body("success", is(true));

        return resp.then().extract().path("data._id");
    }
}

