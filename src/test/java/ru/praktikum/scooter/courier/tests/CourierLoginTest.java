package ru.praktikum.scooter.courier.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.courier.Courier;
import ru.praktikum.scooter.courier.CourierClient;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static utils.SiteUrl.MAIN_PAGE_URL;

public class CourierLoginTest {
    private static final String loginForTest = new Faker().name().username();
    private static final String passwordForTest = new Faker().internet().password();
    private static final String firstNameForTest = new Faker().name().firstName();
    private final CourierClient courierClient = new CourierClient();
    private final Courier courierDelete = new Courier(loginForTest, passwordForTest);

    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI = MAIN_PAGE_URL;
        Courier courier = new Courier(loginForTest, passwordForTest, firstNameForTest);
        courierClient.createCourierStep(courier, 201, true);
    }

    @Test
    @DisplayName("/api/v1/courier/login post: login, password")
    @Description("Успешная авторизация")
    public void courierLoginAndCheckResponse() {
        Courier courier = new Courier(loginForTest, passwordForTest);
        courierClient.loginCourier()
                .assertThat()
                .statusCode(200)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("/api/v1/courier/login post: login null")
    @Description("Не заполнено поле login при авторизации")
    public void courierLoginWithoutLogin() {
        Courier courier = new Courier("", passwordForTest);
        courierClient.loginCourier()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("/api/v1/courier/login post: password null")
    @Description("Не заполнено поле password при авторизации")
    public void courierLoginWithoutPassword() {
        Courier courier = new Courier(loginForTest, "");
        courierClient.loginCourier()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("/api/v1/courier/login post: wrong login")
    @Description("Неверный логин")
    public void courierLoginWithWrongLogin() {
        Courier courier = new Courier(loginForTest + "wrong", passwordForTest);
        courierClient.loginCourier()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("/api/v1/courier/login post: wrong password")
    @Description("Неверный пароль")
    public void courierLoginWithWrongPassword() {
        Courier courier = new Courier(loginForTest, passwordForTest + "wrong");
        courierClient.loginCourier()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierDelete);
    }
}
