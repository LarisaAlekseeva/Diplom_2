import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.Steps;
import praktikum.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    private User user;
    private Steps steps;
    private String accessToken;

    @Before
    public void setUser() {
        user = User.getRandomUser();
        steps = new Steps();
    }

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        ValidatableResponse response = steps.createUser(user);
        response.assertThat().statusCode(200).and().body("success", is(true));
    }

    @Test
    @DisplayName("Создание пользователя с уже существующим логином")
    public void createDuplicateUserTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.createUser(user);
        response.assertThat().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без ввода имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse response = steps.createUser(user);
        response.assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без ввода пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = steps.createUser(user);
        response.assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создать пользователя без ввода элпочты")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse response = steps.createUser(user);
        response.assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void CleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
