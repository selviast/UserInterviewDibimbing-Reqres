package tests.reqres;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GetUserByIdTest {
    private String cookie;

    @BeforeClass
    public void setup() throws Exception {
        // Set base URI dari config.properties
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");
    }

    @Test
    public void getUserByIdTest() throws IOException {
        String idParam = "2";
        Response response = given()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .queryParam("id", ConfigReader.getProperty("idUser"))
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract().response();

        // Print response
        System.out.println("Response: " + response.asString());

        // Validasi status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Status code bukan 200! Response: " + response.asString());

        // Validasi result.id sesuai
        String id = response.jsonPath().getString("data.id");
        Assert.assertEquals(id, idParam, "Id user tidak sesuai");

        String data = response.jsonPath().getString("data.email");
        Assert.assertTrue(data.contains("reqres.in"), "Data email harus mengandung 'reqres.in'");

    }

    @Test
    public void getUserByIdTestNegative() throws IOException {
        Response response = given()
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .header("x-api-key", ConfigReader.getProperty("apiKey"))
                .queryParam("id", ConfigReader.getProperty("idUserNegative"))
                .when()
                .get("/api/users")
                .then()
                .extract().response();

        // Print response
        System.out.println("Response: " + response.asString());

        // Validasi status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404, "Status code bukan 404! Response: " + response.asString());

        // Validasi result
        String body = response.getBody().asString();
        Assert.assertEquals(body, "{}", "Body harus empty");


    }
}
