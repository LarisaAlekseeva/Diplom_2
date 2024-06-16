package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Login;
import pojo.User;

import static io.restassured.RestAssured.given;

public class UserSteps extends ConstantSpec {
    private static final String REGISTER_ENDPOINT = "api/auth/register";
    private static final String LOGIN_ENDPOINT = "api/auth/login";
    private static final String DELETE_USER = "api/auth/user";
    private static final String USER_ENDPOINT = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(REGISTER_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Login login) {
        return given()
                .spec(getSpec())
                .body(login)
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse updateUser(User user, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .and()
                .body(user)
                .when()
                .patch(USER_ENDPOINT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .delete(DELETE_USER)
                .then();
    }
}
