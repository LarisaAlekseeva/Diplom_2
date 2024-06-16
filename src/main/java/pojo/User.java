package pojo;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class User {
    private String email;
    private String password;
    private String name;


    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Step("Ввод данных для создания пользователя")
    public static User getRandomUser() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().username());
    }

    @Step("Ввод данных без email")
    public static User getRandomUserWithoutEmail() {
        Faker faker = new Faker();
        return new User(null, faker.internet().password(), faker.name().username());
    }

    @Step("Ввод данных без пароля")
    public static User getRandomUserWithoutPassword() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), null, faker.name().username());
    }

    @Step("Ввод данных без имени")
    public static User getRandomUserWithoutName() {
        Faker faker = new Faker();
        return new User(null, faker.internet().password(), null);
    }
}
