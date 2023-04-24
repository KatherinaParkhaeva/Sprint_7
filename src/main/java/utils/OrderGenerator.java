package utils;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OrderGenerator {
    static List<String> colourList = List.of("BLACK", "GREY");
    Faker faker = new Faker(new Locale("ru"));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static List<String> getColor() {
        return colourList;
    }

    public String getFirstName() {
        return faker.name().firstName();
    }

    public String getLastName() {
        return faker.name().lastName();
    }

    public String getAddress() {
        return faker.address().fullAddress();
    }

    public String getPhone() {
        return faker.phoneNumber().phoneNumber();
    }

    public int getRentTime() {
        return faker.number().numberBetween(1, 7);
    }

    public String getDeliveryDate() {
        String deliveryDate = sdf.format(faker.date().future(30, TimeUnit.DAYS));
        return deliveryDate;
    }

    public String getComment() {
        return faker.lorem().sentence(3, 3);
    }

    public String getMetroStation() {
        return faker.address().city();
    }
}
