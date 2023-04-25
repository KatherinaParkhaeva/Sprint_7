import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static constants.SiteUrl.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.client.methods.RequestBuilder.delete;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {
    private static String loginForTest = new Faker().name().username();
    private static String passwordForTest = new Faker().internet().password();
    private static String firstNameForTest= new Faker().name().firstName();


    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI= MAIN_PAGE_URL;
    }

    @Test
    @DisplayName("/api/v1/courier post: login, password, firstName")
    @Description("Создание нового курьера с корректными данными")
    public void createNewCourierAndCheckResponse(){
        Courier courierCreate  = new Courier(loginForTest,passwordForTest,firstNameForTest);
        createCourierStep(courierCreate, 201, true);
    }
    @Test
    @DisplayName("/api/v1/courier post: login, password")
    @Description("Создание нового курьера без одного параметра")
    public  void createCourierWithoutFirstName() {
        Courier courierCreate  = new Courier(loginForTest, passwordForTest);
        createCourierStep(courierCreate, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("/api/v1/courier post: login, password, firstName")
    @Description("Попытка создать двух одинаковых курьеров")
    public void createDoubleCourierAndCheckResponse(){
        Courier courierCreate  = new Courier(loginForTest, passwordForTest, firstNameForTest);
        createCourierStep(courierCreate, 201, true);
        createCourierStep(courierCreate, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    // метод для шага "Отправить запрос":
    @Step("Create courier by sending POST request to /api/v1/courier")
    public void createCourierStep (Courier courierCreate, int statusCode, Object body) {
        given()
                .header("Content-type","application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(COURIER_CREATING)
                .then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @After
    public void tearDown() {
        deleteCourier();
    }

    @Step("Delete courier by sending DELETE request to /api/v1/courier/")
    public void deleteCourier(){
        Courier courierDelete=new Courier(loginForTest,passwordForTest);
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
