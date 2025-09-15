package ru.praktikum.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;

public class AllureFilters {
    public static Filter withAllure() {
        return new AllureRestAssured();
    }
}

