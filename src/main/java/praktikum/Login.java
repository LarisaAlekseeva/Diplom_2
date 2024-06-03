package praktikum;

public class Login {
    private String email;
    private String password;

    public Login(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
