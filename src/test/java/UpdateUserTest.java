import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import praktikum.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserTest {
    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();

    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Проверка изменения элпочты авторизованного пользователя")
    public void authUserEmailUpdateTest() {
        User user = User.getRandomUser();
        accessToken = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        String newEmail = user.getEmail().toLowerCase();
        userSteps.updateUser(user, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Проверка изменения имени авторизованного пользователя")
    public void authUserNameUpdateTest() {
        User user = User.getRandomUser();
        accessToken = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUser(user, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Проверка изменения email неавторизованного пользователя")
    public void updateEmailWithoutAuthorizationTest() {
        User user = User.getRandomUser();
        accessToken = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.updateUser(user, "")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка изменения имени неавторизованного пользователя")
    public void updateNameWithoutAuthorizationTest() {
        User user = User.getRandomUser();
        accessToken = userSteps.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUser(user, "")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
