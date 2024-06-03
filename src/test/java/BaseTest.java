import org.junit.After;
import org.junit.Before;
import praktikum.Login;
import praktikum.Steps;
import praktikum.User;

public class BaseTest {
    protected User user;
    protected Login login;
    protected Steps steps;
    protected String accessToken;

    @Before
    public void setOrder() {
        user = User.getRandomUser();
        steps = new Steps();
        login = new Login(user);
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
