package ru.praktikum.scooter.order.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.order.OrderClient;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static utils.SiteUrl.MAIN_PAGE_URL;

public class OrderListTest {
    private final OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI = MAIN_PAGE_URL;
    }

    @Test
    @DisplayName("Get order, GET /api/v1/orders")
    @Description("Проверяем, что список заказов не пустой")
    public void checkNotNullOrderList() {
        orderClient.getOrderList()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
