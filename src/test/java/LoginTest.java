import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Login;
import pojo.User;
import praktikum.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LoginTest {
    private UserSteps userSteps;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken)
                    .assertThat().statusCode(SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Проверка авторизации существующего пользователя")
    public void loginExistingUserIsSuccessTest() {
        User user = User.getRandomUser();
        response = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken");
        Login login = Login.getRandomLogin(user);
        userSteps.loginUser(login)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Логин c неверным email")
    public void loginUserWithInvalidEmailTest() {
        User user = User.getRandomUser();
        response = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        accessToken = response.extract().path("accessToken");
        Login login = Login.getLoginWithInvalidEmail(user);
        userSteps.loginUser(login)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Логин c неверным паролем")
    public void loginUserWithInvalidPasswordTest() {
        User user = User.getRandomUser();
        response = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken");
        Login login = Login.getLoginWithInvalidPassword(user);
        userSteps.loginUser(login)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false));
    }
}
