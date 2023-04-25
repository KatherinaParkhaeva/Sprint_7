import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.*;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.List;

import static constants.SiteUrl.*;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private int orderTrack;
    private List<String> colourList;
    private int expectedStatusCode;

    public OrderCreateTest(List<String> colourList, int expectedStatusCode){
        this.colourList=colourList;
        this.expectedStatusCode=expectedStatusCode;
    }

    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI= MAIN_PAGE_URL;
        //OrderGenerator orderGenerator = new OrderGenerator();
        Order order=new Order()
                .setFirstName(OrderGenerator.getFirstName())
                .setLastName(OrderGenerator.getLastName())
                .setAddress(OrderGenerator.getAddress())
                .setMetroStation(OrderGenerator.getMetroStation())
                .setPhone(OrderGenerator.getPhone())
                .setRentTime(OrderGenerator.getRentTime())
                .setDeliveryDate(OrderGenerator.getDeliveryDate())
                .setComment(OrderGenerator.getComment())
                .setColor(colourList);
    }

    @Parameterized.Parameters(name = "Цвет самоката. Тестовые данные: список цветов {0}, ожидаемый статус-код {1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {List.of("BLACK"), HttpStatus.SC_CREATED},
                {List.of("GREY"), HttpStatus.SC_CREATED},
                {List.of("BLACK", "GREY"), HttpStatus.SC_CREATED},
                {List.of(""), HttpStatus.SC_CREATED},
        };
    }

    @Test
    @DisplayName("Create order, POST /api/v1/orders")
    @Description("Создание заказа с выбором цвета")
    public void createOrderWithOptionalColors() {
        OrderGenerator order=new OrderGenerator();
        ValidatableResponse response = createOrder(order);
        orderTrack = response.extract().path("track");
        assertEquals("Неверный статус код",
                expectedStatusCode,
                response.extract().statusCode());
        assertNotNull("Неверное значение", orderTrack);
    }



    @Step("Создание заказа")
    public ValidatableResponse createOrder(OrderGenerator order) {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_CREATE_AND_LIST)
                .then();
        return response;
    }

    @After
    public void tearDown() {
        if (orderTrack != 0) {
            cancelOrder(orderTrack);
        }
    }
    @Step("Отмена заказа")
    public void cancelOrder(int track) {
        given()
                .queryParam("track", track)
                .when()
                .put(ORDER_CANCEL)
                .then();
    }
}
