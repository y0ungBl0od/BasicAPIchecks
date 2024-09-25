import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class BebsTests {

    @Test
    public void tokenZipUserNameCheck() {

        // Test data
        int userId = 87;
        String bodyToken = "{\"operationName\":null,\"variables\":{},\"query\":\"mutation {\\n  token(id: " + userId + ")\\n}\\n\"}";
        String tokenExpected = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidGsyWjZZRXZyZWhXaXNBam5XYXFMckg3Z1FuMSJ9.no4p57l7a3gZVNX_sfimvnnNTn8wBus7cfW8-H9g_RI";
        String bodyProfile = "{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  profile {\\n    id\\n    name\\n    zip\\n    rating\\n    today {\\n      description\\n      created_at\\n    }\\n  }\\n}\\n\"}";
        String zipExpected = "90189";
        String baseURL = "https://dev-api.bebs.app/graphql";
        String userExpected = "Emil mainn";

        // receive token
        String response = given()
                .body(bodyToken)
                .when().post(baseURL)
                .then().extract().response().asString();
//        System.out.println(response);

        // Assert
        String tokenReceived = new JsonPath(response).getString("data.token");
        Assert.assertEquals(tokenReceived, tokenExpected);
        System.out.println("Token Test Passed");


        // receive response query Profile
        String response2 = given().contentType(ContentType.JSON).header("Authorization", tokenReceived)
                .body(bodyProfile)
                .when().post(baseURL)
                .then().extract().response().asString();
//        System.out.println(response2);

//        public void getProfileHasStatusCode200 {
//            given().header("Content-Type", "application/json").header("Authorization", tokenReceived)
//                    .body(bodyProfile).
//                    when().
//                    post()
//                    .then()
//                    .statusCode(200);

            String zipCodeReceived = new JsonPath(response2).getString("data.profile.zip");
            String userNameIsEmil = new JsonPath(response2).getString("data.profile.name");
            Assert.assertEquals(zipCodeReceived, zipExpected);
            System.out.println("Zip Test Passed");
            Assert.assertEquals(userNameIsEmil,userExpected);
            System.out.println("Name Test Passed");


        }
    }


