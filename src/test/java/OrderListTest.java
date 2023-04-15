import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static constants.SiteUrl.MAIN_PAGE_URL;
import static constants.SiteUrl.ORDER_CREATE_AND_LIST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI= MAIN_PAGE_URL;
    }

    @Test
    @DisplayName("Get order, GET /api/v1/orders")
    @Description("Проверяем, что список заказов не пустой")
    public void checkNotNullOrderList() {
        given()
                .when()
                .get(ORDER_CREATE_AND_LIST)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
