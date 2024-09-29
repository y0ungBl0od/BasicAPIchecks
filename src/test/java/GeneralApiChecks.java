import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;


public class GeneralApiChecks {


    @Test
    public void checkEmailEndsWithDomain() {
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL),Specifications.rsSpecCode200OK());
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
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL),Specifications.rsSpecCode200OK());
        Integer expectedId = 4;
        String expectedToken = "QpwL5tke4Pnpja7X4";


        UserRegistrationBody user = new UserRegistrationBody(TestData.EMAIL, TestData.PASSWORD);
        SuccessRegResponse response = given()
                .body(user).log().body()
                .when()
                .post("api/register")
                .then().log().status().log().body()
                .extract().as(SuccessRegResponse.class);
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getToken());

        Assert.assertEquals(expectedId, response.getId());
        Assert.assertEquals(expectedToken, response.getToken());
        System.out.println("Registration is successful");
    }

    @Test
    public void registrationFailCheck (){
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL),Specifications.resSpecCodeError400());


        UserRegistrationBody user = new UserRegistrationBody(TestData.emailGenerated,"");

        UnsuccessRegResponse error = given()
                .body(user).log().body()
                .when()
                .post("api/register")
                .then().log().status().log().body()
                .extract().as(UnsuccessRegResponse.class);

        Assert.assertEquals(TestData.errorMessage,error.getError());
        System.out.println("Not registered without password - OK");
        System.out.println("qa branch created");
    }

    @Test
    public void yearsSorted() {
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL), Specifications.rsSpecCode200OK());

        List<ListOfColors> colorsByYears = new ArrayList<>();

        colorsByYears = given()
                .when()
                .get("api/unknown")
                .then().log().status()
                .extract().body().jsonPath().getList("data", ListOfColors.class);

        List<Integer> years = colorsByYears.stream().map(ListOfColors::getYear).toList();
        List<Integer> sortedYears = years.stream().sorted().toList();
        System.out.println(years + "\n" + sortedYears);


        Assert.assertEquals(sortedYears, years);
        System.out.println("Received list is sorted by years");

    }

    @Test
    public void deleteUserCheck() {
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL), Specifications.rsSpecCodeUnique(204));

            given()
                .when()
                .delete("api/users/2")
                .then().log().status();


    }

    @Test
    public  void checkUpdateUserResponseTime(){
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL), Specifications.rsSpecCode200OK());

        UserUpdate user = new UserUpdate("morpheus", "zion resident");
        UserUpdateResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().status().log().body()
                .extract().response().as(UserUpdateResponse.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneId.of("UTC"));
        String currentTime = formatter.format(Instant.now());
        String receivedTime = response.getUpdatedAt().substring(0,response.getUpdatedAt().length()-5);

        Assert.assertEquals(currentTime,receivedTime);
    }

    @Test
    public void responseNameJobCheck() {
        Specifications.installSpecification(Specifications.reqSpec(TestData.BASE_URL), Specifications.rsSpecCode200OK());
        final String inputName = RandomStringUtils.random(5, true, true);
        ;
        final String inputJob = RandomStringUtils.random(5, true, true);
        ;
        UserUpdate user = new UserUpdate(inputName, inputJob);

        UserUpdateResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().body()
                .extract().response().as(UserUpdateResponse.class);

        String name = response.getName();
        String job = response.getJob();

        Assert.assertEquals(inputName, name);
        Assert.assertEquals(inputJob, job);
    }
}

