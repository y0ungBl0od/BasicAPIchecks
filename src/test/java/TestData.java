import org.apache.commons.lang3.RandomStringUtils;

public class TestData {
    protected final static String BASE_URL = "https://reqres.in/";
    protected static final String EMAIL = "eve.holt@reqres.in";
    protected static final String PASSWORD = "pistol";
    protected static final String emailGenerated = RandomStringUtils.random(6,true,true);
    protected static final String errorMessage = "Missing password";
}
