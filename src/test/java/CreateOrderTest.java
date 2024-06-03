import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest extends BaseTest {

    @Test
    @DisplayName("Cоздание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse response = steps.createOrderWithoutLogin();
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthAndIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse response1 = steps.createOrderLoggedUser(accessToken);
        response1.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хешем ингредиентов")
    public void createOrderWithWrongIngredientHashTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderWithWrongIngredientHash(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void createOrderWithAuthAndWithoutIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderWithoutIngredients(accessToken);

    }
}
