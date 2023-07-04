import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;



public class BebsTest {

    public static void main(String[] args) {

        String response = given()
                .header("Content-type", "Application/json")
                .header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoielp6cDZ6UHBocE51RVZ1N2FmZHhDaHFmcGpyMSJ9.uuVnWh36PlmsTTNRykldWikw1Qq9i0R66uumDBFF7Ls")
                .body("{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  profiles(\\n    where: {isCardHolder: false, genderFilter: [NONBINARY], distance: 80000, birthday: [18, 57]}\\n    paginate: {limit: 1000}\\n  ) {\\n    id\\n    name\\n    gender\\n  }\\n}\\n\"}")
                .when().post("http://217.107.219.153:3000/graphql")
                .then().extract().response().asString();
        System.out.println(response);

        JsonPath js = new JsonPath(response);
        String x = js.getString("data.profiles.id[0]");
        Assert.assertEquals(x, "25");
    }

}