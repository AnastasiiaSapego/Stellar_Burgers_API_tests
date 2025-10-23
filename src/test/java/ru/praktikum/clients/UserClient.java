package ru.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import ru.praktikum.config.Endpoints;
import ru.praktikum.config.TestConfig;
import ru.praktikum.models.Credentials;
import ru.praktikum.models.User;

import java.util.Map;

public class UserClient {

    @Step("POST {TestConfig.REGISTER}: регистрация пользователя")
    public Response register(User user) {
        return given()
                .spec(TestConfig.getBaseRequestSpec())
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("POST {TestConfig.REGISTER}: регистрация с произвольным телом")
    public Response registerRaw(Map<String, Object> body) {
        return given()
                .spec(TestConfig.getBaseRequestSpec())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("POST {TestConfig.LOGIN}: логин пользователя")
    public Response login(Credentials credentials) {
        return given()
                .spec(TestConfig.getBaseRequestSpec())
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(Endpoints.LOGIN);
    }

    @Step("DELETE {TestConfig.USER}: удалить пользователя (нужна Authorization)")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(TestConfig.getBaseRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER);
    }
}


