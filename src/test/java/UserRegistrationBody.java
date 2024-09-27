public class UserRegistrationBody {
    private String email;
    private String password;

    public UserRegistrationBody(String email, String password) {
        this.email = String.valueOf(email);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
