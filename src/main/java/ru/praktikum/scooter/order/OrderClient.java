package ru.praktikum.scooter.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static utils.SiteUrl.ORDER_CANCEL;
import static utils.SiteUrl.ORDER_CREATE_AND_LIST;

public class OrderClient {
    private Order order;

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_CREATE_AND_LIST)
                .then();
    }

    @Step("Вызов листа заказа")
    public ValidatableResponse getOrderList() {
        return given()
                .when()
                .get(ORDER_CREATE_AND_LIST)
                .then();
    }

    @Step("Отмена заказа")
    public void cancelOrder(int orderTrack) {
        given()
                .queryParam("track", orderTrack)
                .when()
                .put(ORDER_CANCEL)
                .then();
    }
}
