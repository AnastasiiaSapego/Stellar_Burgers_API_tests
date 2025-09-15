package ru.praktikum.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public final class TestConfig {

    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public static final String REGISTER    = "/api/auth/register";
    public static final String LOGIN       = "/api/auth/login";
    public static final String USER        = "/api/auth/user";
    public static final String INGREDIENTS = "/api/ingredients";
    public static final String ORDERS      = "/api/orders";

    public static final String CONTENT_TYPE_JSON = "application/json";

    private static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(JSON)
            .build();

    private TestConfig() {}

    public static RequestSpecification getBaseRequestSpec() {
        return BASE_SPEC;
    }
}

