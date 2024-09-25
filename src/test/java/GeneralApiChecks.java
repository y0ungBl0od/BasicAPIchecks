import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;


public class GeneralApiChecks {
    public final static String BASE_URL = "https://reqres.in/";

    @Test
    public void checkEmailEndsWithDomain() {
        Integer page = 1;
        Integer totalPages;

        List<UsersInfo> allUsers = new ArrayList<>();

        do {
            Response response = given()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(BASE_URL + "api/users?page=" + page)
                    .then().log().body()
                    .extract().response();

            List<UsersInfo> users = response.jsonPath().getList("data", UsersInfo.class);
            allUsers.addAll(users);

            totalPages = response.jsonPath().getInt("total_pages");
            page++;
        }
        while(page <= totalPages);

        allUsers.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(allUsers.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
        System.out.println("All emails contain %@reqres.in");
    }



}
