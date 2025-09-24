package ru.praktikum.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;
import static ru.praktikum.config.Endpoints.BASE_URI;

public final class TestConfig {

    private static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(JSON)
            .build();

    private TestConfig() {}

    public static RequestSpecification getBaseRequestSpec() {
        return BASE_SPEC;
    }
}

