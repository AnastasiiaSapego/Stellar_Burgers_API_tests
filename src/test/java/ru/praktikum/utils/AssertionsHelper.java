package ru.praktikum.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class AssertionsHelper {

    @Step("Проверить успешную регистрацию пользователя (200 OK)")
    public void assertRegistrationSuccess(Response resp) {
        resp.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", not(isEmptyOrNullString()))
                .body("user.name", not(isEmptyOrNullString()))
                .body("accessToken", not(isEmptyOrNullString()))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

    @Step("Проверить ошибку: пользователь уже существует (403 FORBIDDEN)")
    public void assertUserAlreadyExists(Response resp) {
        resp.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    @Step("Проверить ошибку: отсутствует обязательное поле при регистрации (403 FORBIDDEN)")
    public void assertMissingFieldsOnRegistration(Response resp) {
        resp.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    @Step("Проверить успешный логин пользователя (200 OK)")
    public void assertLoginSuccess(Response resp) {
        resp.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("user.email", not(isEmptyOrNullString()))
                .body("accessToken", not(isEmptyOrNullString()))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

    @Step("Проверить ошибку авторизации при логине (401 UNAUTHORIZED)")
    public void assertLoginUnauthorized(Response resp) {
        resp.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    @Step("Проверить: заказ с авторизацией успешно создан (200 OK)")
    public void assertOrderCreated(Response resp) {
        resp.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", not(isEmptyOrNullString()))
                .body("order.number", notNullValue());
    }

    @Step("Проверить: заказ без авторизации успешно создан (200 OK)")
    public void assertOrderUnauthorized(Response resp) {
        resp.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", not(isEmptyOrNullString()))
                .body("order.number", notNullValue());
    }

    @Step("Проверить ошибку: заказ без ингредиентов (400 BAD_REQUEST)")
    public void assertOrderNoIngredients(Response resp) {
        resp.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    @Step("Проверить ошибку: заказ с невалидным ингредиентом (500 INTERNAL_SERVER_ERROR)")
    public void assertOrderInvalidIngredientHash(Response resp) {
        resp.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
