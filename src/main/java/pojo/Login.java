package pojo;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class Login {
    private String email;
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Step("Ввод персональных данных")
    public static Login getRandomLogin(User user) {
        Login login = new Login();
        login.setEmail(user.getEmail());
        login.setPassword(user.getPassword());
        return login;
    }

    @Step("Ввод данных с неверным email")
    public static Login getLoginWithInvalidEmail(User user) {
        Login login = new Login();
        Faker faker = new Faker();
        login.setEmail(faker.internet().emailAddress());
        login.setPassword(user.getPassword());
        return login;
    }

    @Step("Ввод данных с неверным паролем")
    public static Login getLoginWithInvalidPassword(User user) {
        Login login = new Login();
        Faker faker = new Faker();
        login.setEmail(user.getEmail());
        login.setPassword(faker.internet().password());
        return login;
    }
}
