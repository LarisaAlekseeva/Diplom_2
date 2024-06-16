import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import praktikum.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
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
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        User user = User.getRandomUser();
        response = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя с уже существующим логином")
    public void createDuplicateUserTest() {
        User user = User.getRandomUser();
        response = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken");
        userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание пользователя без ввода имени")
    public void createUserWithoutNameTest() {
        User user = User.getRandomUserWithoutName();
        response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создать пользователя без ввода пароля")
    public void createUserWithoutPasswordTest() {
        User user = User.getRandomUserWithoutPassword();
        response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создать пользователя без ввода элпочты")
    public void createUserWithoutEmailTest() {
        User user = User.getRandomUserWithoutEmail();
        response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false));
    }
}
