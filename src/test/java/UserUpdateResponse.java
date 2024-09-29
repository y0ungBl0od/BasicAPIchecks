import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;

public class UserUpdateResponse extends UserUpdate{
    private String updatedAt;

    public UserUpdateResponse(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public UserUpdateResponse() {}

    public String getUpdatedAt() {
        return updatedAt;
    }


}
