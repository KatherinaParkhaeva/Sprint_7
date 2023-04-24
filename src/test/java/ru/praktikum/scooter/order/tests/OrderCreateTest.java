package ru.praktikum.scooter.order.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.scooter.order.Order;
import ru.praktikum.scooter.order.OrderClient;
import utils.OrderGenerator;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static utils.SiteUrl.MAIN_PAGE_URL;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private Order order;
    private final OrderClient orderClient = new OrderClient();
    private final OrderGenerator orderGenerator = new OrderGenerator();
    private final List<String> colourList;
    private final int expectedStatusCode;
    private int orderTrack;

    public OrderCreateTest(List<String> colourList, int expectedStatusCode) {
        this.colourList = colourList;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Цвет самоката. Тестовые данные: список цветов {0}, ожидаемый статус-код {1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {List.of("BLACK"), HttpStatus.SC_CREATED},
                /*{List.of("GREY"), HttpStatus.SC_CREATED},
                {List.of("BLACK", "GREY"), HttpStatus.SC_CREATED},
                {List.of(""), HttpStatus.SC_CREATED},*/
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = MAIN_PAGE_URL;
        order = new Order()
                .setFirstName(orderGenerator.getFirstName())
                .setLastName(orderGenerator.getLastName())
                .setAddress(orderGenerator.getAddress())
                .setMetroStation(orderGenerator.getMetroStation())
                .setPhone(orderGenerator.getPhone())
                .setRentTime(orderGenerator.getRentTime())
                .setDeliveryDate(orderGenerator.getDeliveryDate())
                .setComment(orderGenerator.getComment())
                .setColour(colourList);
    }

    @Test
    @DisplayName("Create order, POST /api/v1/orders")
    @Description("Создание заказа с выбором цвета")
    public void createOrderWithOptionalColors() {
        order = order.setColour(colourList);
        ValidatableResponse response = orderClient.createOrder(order);
        orderTrack = response.extract().path("track");
        assertEquals("Неверный статус код",
                expectedStatusCode,
                response.extract().statusCode());
        assertNotNull("Неверное значение", orderTrack);
    }

    @After
    public void tearDown() {
        if (orderTrack != 0) {
            orderClient.cancelOrder(orderTrack);
        }
    }

}
