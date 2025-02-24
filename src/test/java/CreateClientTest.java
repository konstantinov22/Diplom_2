import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.Client;
import constant.Random;
import client.ClientOperations;
import constant.Constants;

import static org.hamcrest.CoreMatchers.*;

public class CreateClientTest {

    Client client;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        client = Random.generateUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            ClientOperations.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание нового пользователя, используя валидные данные")
    @Description("Создание нового пользователя, используя валидные данные")
    public void createNewUserTestGetSuccess() {
        Response response = ClientOperations.createUser(client);
        // accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Создать пользователя, который уже зарегистрирован")
    public void createTwoSimilarUsersTestGetError() {
        Response response = ClientOperations.createUser(client);
        // accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        ClientOperations.createUser(client)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создайте пользователя без электронного адреса")
    @Description("Создайте пользователя без электронного адреса")
    public void createUserWithoutEmailTestGetError() {
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        client = new Client(password, name);
        ClientOperations.createUser(client)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создайте пользователя без пароля")
    @Description("Создайте пользователя без пароля")
    public void createUserWithoutPasswordTestGetError() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String name = RandomStringUtils.randomAlphabetic(8);
        client = new Client(email, name);
        ClientOperations.createUser(client)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создайте пользователя без имени")
    @Description("Создайте пользователя без имени")
    public void createUserWithoutNameTestGetError() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(8);
        client = new Client(email, password);
        ClientOperations.createUser(client)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}