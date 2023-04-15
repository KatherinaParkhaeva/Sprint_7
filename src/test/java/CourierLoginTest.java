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
import static constants.SiteUrl.COURIER_DELETE;
import static io.restassured.RestAssured.given;
import static org.apache.http.client.methods.RequestBuilder.delete;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private static String loginForTest = new Faker().name().username();
    private static String passwordForTest = new Faker().internet().password();
    private static String firstNameForTest= new Faker().name().firstName();

    @Before
    public void setUp() throws IOException {
        RestAssured.baseURI= MAIN_PAGE_URL;
        Courier courierCreate=new Courier(loginForTest,passwordForTest,firstNameForTest);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreate)
                .when()
                .post(COURIER_CREATING)
                .then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("/api/v1/courier/login post: login, password")
    @Description("Успешная авторизация")
    public void courierLoginAndCheckResponse(){
        Courier courierLogin  = new Courier(loginForTest, passwordForTest);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN)
                .then().assertThat().statusCode(200)
                .and()
                .body("id", notNullValue());;
    }

    @Test
    @DisplayName("/api/v1/courier/login post: login null")
    @Description("Не заполнено поле login при авторизации")
    public  void courierLoginWithoutLogin() {
        Courier courierCreate  = new Courier("", passwordForTest);
        given()
                .body(courierCreate)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(400) //Expected status code <400> but was <504>
                .and()
                .body("message", equalTo("Недостаточно данных для входа")); //Service unavailable
    }
    @Test
    @DisplayName("/api/v1/courier/login post: password null")
    @Description("Не заполнено поле password при авторизации")
    public  void courierLoginWithoutPassword() {
        Courier courierCreate  = new Courier(loginForTest, "");
        given()
                .body(courierCreate)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(400) //Expected status code <400> but was <504>
                .and()
                .body("message", equalTo("Недостаточно данных для входа")); //Service unavailable
    }

    @Test
    @DisplayName("/api/v1/courier/login post: wrong login")
    @Description("Неверный логин")
    public  void courierLoginWithWrongLogin() {
        Courier courierCreate  = new Courier(loginForTest + "wrong", passwordForTest);
        given()
                .body(courierCreate)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(404) //Expected status code <404> but was <504>
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("/api/v1/courier/login post: wrong password")
    @Description("Неверный пароль")
    public  void courierLoginWithWrongPassword() {
        Courier courierCreate  = new Courier(loginForTest, passwordForTest + "wrong");
        given()
                .body(courierCreate)
                .when()
                .post(COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(404) //Expected status code <404> but was <504>
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
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
