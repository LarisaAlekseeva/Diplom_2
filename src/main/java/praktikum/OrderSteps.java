package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps extends ConstantSpec {
    public static final String ORDER_ENDPOINT = "api/orders";
    public static final String INGREDIENTS_ENDPOINT = "api/ingredients";

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrderLoggedUser(Order ingredients, String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .body(ingredients)
                .post(ORDER_ENDPOINT)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutLogin(Order ingredients) {
        return given()
                .spec(getSpec())
                .body(ingredients)
                .post(ORDER_ENDPOINT)
                .then();
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getSpec())
                .get(ORDER_ENDPOINT)
                .then();
    }

    @Step("Получение списка заказов без авторизации")
    public ValidatableResponse getListOfOrdersWithoutUserToken() {
        return given()
                .spec(getSpec())
                .get(ORDER_ENDPOINT)
                .then();
    }

    @Step("Получение списка всех ингредиентов")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getSpec())
                .get(INGREDIENTS_ENDPOINT)
                .then();
    }
}
