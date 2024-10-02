import groovyjarjarpicocli.CommandLine;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GeneralApiChecksNoPOJO extends TestData{
    Integer page = 1;

    Integer totalPages;
    @Test
    public void checkAvatarsNoPojo () {
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL), Specifications.rsSpecCode200OK());

        List<String> allEmails = new ArrayList<>();
        List<Integer> allIds = new ArrayList<>();
        Response response;
        do {
            response = given()
                    .when()
                    .get("api/users?page=" + page)
                    .then().log().body()
                    .body("page", equalTo(page))
                    .body("data.id", notNullValue())
                    .body("data.email", notNullValue())
                    .body("data.avatar", notNullValue())
                    .extract().response();

            totalPages = response.jsonPath().getInt("total_pages");
            page++;
            JsonPath jsonPath = response.jsonPath();
            List<String> emails = jsonPath.getList("data.email");
            List<Integer> ids = jsonPath.getList("data.id");
            allEmails.addAll(emails);
            allIds.addAll(ids);


        }
        while (page <= totalPages);

        Assert.assertTrue(allEmails.stream().allMatch(x->x.endsWith("@reqres.in")));
        System.out.println(String.join("\n", allEmails));

    }

    @Test
    public void succesRegistration(){
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL), Specifications.rsSpecCode200OK());
        String expectedToken = "QpwL5tke4Pnpja7X4";

        Map<String, String> userInput = new HashMap<>();
        userInput.put("email", TestData.EMAIL);
        userInput.put("password", TestData.PASSWORD);
        ValidatableResponse response = (ValidatableResponse) given()
                .body(userInput)
                .when()
                .post("api/register")
                .then().log().status().log().body()
                .body("id", equalTo(4))             //удалить при альтерантивном методе
                .body("token", equalTo(expectedToken))      // удалить при альтерантивном методе
                .extract().response();

//  Альтернативно через Assert

//        JsonPath jsonPath = response.extract().jsonPath();
//
//        int idReceived = jsonPath.getInt("id");
//        String tokenReceived = jsonPath.get("token");
//
//        Assert.assertEquals(expectedToken,tokenReceived);
//        Assert.assertEquals(4,idReceived);
    }

    @Test
    public void unsucRegNoPojo(){
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL),Specifications.rsSpecCodeManual(400));
        Map<String, String> userInput = new HashMap<>();
        userInput.put("email",TestData.emailGenerated);

        Response response = given()
                .body(userInput).log().body()
                .when()
                .post("api/register")
                .then().log().body()
                .body("error",equalTo(TestData.errorMessage))
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        Assert.assertEquals(TestData.errorMessage,error);
    }
}

