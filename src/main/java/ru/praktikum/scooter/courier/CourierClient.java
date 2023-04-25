package ru.praktikum.scooter.courier;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.client.methods.RequestBuilder.delete;
import static org.hamcrest.CoreMatchers.equalTo;
import static utils.SiteUrl.*;

public class CourierClient {
    private Courier courier;
    private Courier courierDelete;

    @Step("Create courier by sending POST request to /api/v1/courier")
    public ValidatableResponse createCourierStep(Courier courier, int statusCode, Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_CREATING)
                .then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Step("Login courier by sending POST request to /api/v1/courier/login")
    public ValidatableResponse loginCourier() {
        return given()
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Delete courier by sending DELETE request to /api/v1/courier/")
    public void deleteCourier(Courier courierDelete) {
        //Courier courierDelete=new Courier(loginForTest,passwordForTest);
        String response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierDelete)
                .when()
                .post(COURIER_DELETE)
                .asString();

        JsonPath jsonPath = new JsonPath(response);
        String userId = jsonPath.getString("id");
        delete(COURIER_DELETE + userId);
    }
}
