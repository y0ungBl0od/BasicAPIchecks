import groovyjarjarpicocli.CommandLine;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;


public class GeneralApiChecks {
    private final static String BASE_URL = "https://reqres.in/";
    private static final String EMAIL = "eve.holt@reqres.in";
    private static final String PASSWORD = "pistol";
    private static final String emailGenerated = RandomStringUtils.random(6,true,true);
    private static final String errorMessage = "Missing password";

    @Test
    public void checkEmailEndsWithDomain() {
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL),Specifications.rsSpecCode200OK());
        Integer page = 1;
        Integer totalPages;

        List<UsersInfo> allUsers = new ArrayList<>();

        do {
            Response response = given()
                    .when()
                    .get("api/users?page=" + page)
                    .then()
                    .extract().response();

            List<UsersInfo> users = response.jsonPath().getList("data", UsersInfo.class);
            allUsers.addAll(users);

            totalPages = response.jsonPath().getInt("total_pages");
            page++;
        }
        while(page <= totalPages);

        allUsers.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(allUsers.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> allEmails = allUsers.stream().map(UsersInfo::getEmail).collect(Collectors.toList());
        System.out.println("Emails are: "+"\n");
        System.out.println(String.join("\n", allEmails));
        System.out.println("\n"+ "All emails contain %@reqres.in"+"\n");
    }


    @Test
    public void userRegistrationSuccess (){
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL),Specifications.rsSpecCode200OK());
        Integer expectedId = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";


        UserRegistrationBody user = new UserRegistrationBody(EMAIL, PASSWORD);
        SuccessRegResponse response = given()
                .body(user).log().body()
                .when()
                .post("api/register")
                .then().log().status().log().body()
                .extract().as(SuccessRegResponse.class);

        Assert.assertEquals(expectedId, response.getId());
        Assert.assertEquals(expectedToken, response.getToken());
        System.out.println("Registration is successful");
    }

    @Test
    public void registrationFailCheck (){
        Specifications.installSpecification(Specifications.reqSpec(BASE_URL),Specifications.resSpecCodeError400());


        UserRegistrationBody user = new UserRegistrationBody(emailGenerated,"");

        UnsuccessRegResponse error = given()
                .body(user).log().body()
                .when()
                .post("api/register")
                .then().log().status().log().body()
                .extract().as(UnsuccessRegResponse.class);

        Assert.assertEquals(errorMessage,error.getError());
        System.out.println("Not registered without password - OK");
        System.out.println("OOOOK");
    }
}
