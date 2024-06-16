import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import praktikum.OrderSteps;
import praktikum.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private User user;
    private Order order;
    private List<String> ingredientsList;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        user = User.getRandomUser();
        accessToken = userSteps.createUser(user).extract().path("accessToken");
        ingredientsList = orderSteps.getAllIngredients().extract().path("data._id");
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthAndIngredientsTest() {
        order = new Order(Order.getRandomIngredients(ingredientsList));
        orderSteps.createOrderLoggedUser(order, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    public void createOrderWithoutAuthorizationAndWithIngredientsTest() {
        order = new Order(Order.getRandomIngredients(ingredientsList));
        orderSteps.createOrderWithoutLogin(order)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthTest() {
        order = new Order(Order.getRandomIngredients(null));
        orderSteps.createOrderWithoutLogin(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и невалидным хешем ингредиентов")
    public void createOrderWithNotValidHashIngredientsTest() {
        List<String> newIngredients = List.of("-", "61c0c5a71d1f82001bdaa");
        order = new Order(newIngredients);
        orderSteps.createOrderLoggedUser(order, accessToken)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и некорректным хешем ингредиентов")
    public void createOrderWithWrongIngredientHashTest() {
        List<String> newIngredients = List.of("61c0c5a70d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6p");
        order = new Order(Order.getRandomIngredients(newIngredients));
        orderSteps.createOrderLoggedUser(order, accessToken)
                .assertThat()
                .statusCode(400)
                .and().body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthAndWithoutIngredientsTest() {
        order = new Order(Order.getRandomIngredients(null));
        orderSteps.createOrderLoggedUser(order, accessToken)
                .assertThat()
                .statusCode(400)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));

    }
}
