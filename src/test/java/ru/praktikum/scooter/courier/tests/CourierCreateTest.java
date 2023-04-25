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

import static utils.SiteUrl.MAIN_PAGE_URL;

public class CourierCreateTest {
    private static final String loginForTest = new Faker().name().username();
    private static final String passwordForTest = new Faker().internet().password();
    private static final String firstNameForTest = new Faker().name().firstName();
    private final CourierClient courierClient = new CourierClient();
    private final Courier courierDelete = new Courier(loginForTest, passwordForTest);


    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI = MAIN_PAGE_URL;
    }

    @Test
    @DisplayName("/api/v1/courier post: login, password, firstName")
    @Description("Создание нового курьера с корректными данными")
    public void createNewCourierAndCheckResponse() {
        Courier courier = new Courier(loginForTest, passwordForTest, firstNameForTest);
        courierClient.createCourierStep(courier, 201, true);
    }

    @Test
    @DisplayName("/api/v1/courier post: login, password")
    @Description("Создание нового курьера без одного параметра")
    public void createCourierWithoutFirstName() {
        Courier courier = new Courier(loginForTest, passwordForTest);
        courierClient.createCourierStep(courier, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("/api/v1/courier post: login, password, firstName")
    @Description("Попытка создать двух одинаковых курьеров")
    public void createDoubleCourierAndCheckResponse() {
        Courier courier = new Courier(loginForTest, passwordForTest, firstNameForTest);
        courierClient.createCourierStep(courier, 201, true);
        courierClient.createCourierStep(courier, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierDelete);
    }

}
