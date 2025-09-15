package ru.praktikum.utils;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class AssertionsHelper {

    public void assertRegistrationSuccess(Response resp) {
        resp.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", not(isEmptyOrNullString()))
                .body("user.name", not(isEmptyOrNullString()))
                .body("accessToken", not(isEmptyOrNullString()))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

    public void assertUserAlreadyExists(Response resp) {
        resp.then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    public void assertMissingFieldsOnRegistration(Response resp) {
        resp.then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    public void assertLoginSuccess(Response resp) {
        resp.then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", not(isEmptyOrNullString()))
                .body("accessToken", not(isEmptyOrNullString()))
                .body("refreshToken", not(isEmptyOrNullString()));
    }

    public void assertLoginUnauthorized(Response resp) {
        resp.then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    public void assertOrderCreated(Response resp) {
        resp.then()
                .statusCode(200)
                .body("success", is(true))
                .body("name", not(isEmptyOrNullString()))
                .body("order.number", notNullValue());
    }

    public void assertOrderUnauthorized(Response resp) {
        resp.then()
                .statusCode(anyOf(is(401), is(403)))
                .body("success", is(false));
    }

    public void assertOrderNoIngredients(Response resp) {
        resp.then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", not(isEmptyOrNullString()));
    }

    public void assertOrderInvalidIngredientHash(Response resp) {
        resp.then()
                .statusCode(500);
    }
}
