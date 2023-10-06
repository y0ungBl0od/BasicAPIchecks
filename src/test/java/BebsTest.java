import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;



public class BebsTest {

    public static void main(String[] args) {

        int userId = 50774;
        // token
        String response =  given().log().all().header("Content-Type","application/json")
        .body("{\"operationName\":null,\"variables\":{},\"query\":\"mutation {\\n  token(id: "+userId+")\\n}\\n\"}")
                .when().post("https://api.bebs.app/graphql")
                .then().extract().response().asString();
        System.out.println(response);

        JsonPath token = new JsonPath(response);
        String tokenValue = token.getString("data.token");
        Assert.assertEquals(tokenValue,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiM2lnRmUwUHRnMmR5SGtyWlBZV0tGRFB6UkMwMyJ9.mkmqXZ4BB6UKY3opb2ounr5mAB8P4WBrZpf-ylKp_vI");
    }

}