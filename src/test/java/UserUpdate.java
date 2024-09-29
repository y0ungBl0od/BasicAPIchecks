import java.security.PublicKey;

public class UserUpdate {
    private String name;
    private String job;

    public UserUpdate(String name, String job) {
        this.name = name;
        this.job = job;
    }
    public UserUpdate(){}

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
