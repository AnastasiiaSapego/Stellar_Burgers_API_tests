# Stellar Burgers API tests

## Технологии
| Компонент | Версия |
|---|---|
| Java | 11 |
| Maven | 3.9.x |
| JUnit | 4.13.2 |
| REST Assured | 5.5.6 |
| Allure | 2.29.1 |
| Jackson Databind | 2.17.2 |

## Проект
Набор автотестов API сервиса **Stellar Burgers**:
- регистрация и логин пользователя;
- создание заказов (c авторизацией и без);
- негативные проверки.

Базовый URL: `https://stellarburgers.nomoreparties.site`

## Запуск тестов
```bash
mvn clean test
