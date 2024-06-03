import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest extends BaseTest {

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAuthUserOrdersTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderLoggedUser(accessToken);
        steps.getUserOrders(accessToken);
    }

    @Test
    @DisplayName("Получение заказов не авторизованного пользователя")
    public void getNotAuthUserOrdersTest() {
        ValidatableResponse response = steps.getListOfOrdersWithoutUserToken();
        response.assertThat().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }
}
