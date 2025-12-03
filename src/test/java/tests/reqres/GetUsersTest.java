package tests.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.Argument;
import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class GetUsersTest {

    private String cookie;

    @BeforeClass
    public void setup() throws Exception {
        // Set base URI dari config.properties
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");
    }

    @Test
    public void getUserTest() throws IOException {

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .queryParam("page", ConfigReader.getProperty("pageParam"))
                .when()
                .get("/api/users")
                .then()
                .body("data.last_name.size()", greaterThan(0))
                .body("data.avatar.size()", greaterThan(0))
                .extract().response();

        // Print response
        System.out.println("Response: " + response.asString());

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Status code bukan 200! Response: " + response.asString());

        // validasi page=2
        String page = response.jsonPath().getString("page");
        Assert.assertEquals(page,"2", "Data should not be null");

        // validasi per_page=6
        String per_page = response.jsonPath().getString("per_page");
        Assert.assertEquals(per_page,"6", "Data should not be null");

        // validasi data length=6
        List<Object> dataList = response.jsonPath().getList("data");
        Assert.assertEquals(dataList.toArray().length, 6, "Data should have 6 elements");

    }
}
