package ru.praktikum.models;

public class Ingredient {
    private String id;

    public Ingredient() { }

    public Ingredient(String id) {
        this.id = id;
    }
    public String get_id() {
        return id;
    }
    public void set_id(String id) {
        this.id = id;
    }
}
