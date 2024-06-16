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

public class GetOrderTest {
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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersAuthorizedUserTest() {
        order = new Order(Order.getRandomIngredients(ingredientsList));
        orderSteps.createOrderLoggedUser(order, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
        orderSteps.getUserOrders(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersNotAuthorizedUserTest() {
        order = new Order(Order.getRandomIngredients(ingredientsList));
        orderSteps.getListOfOrdersWithoutUserToken()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
