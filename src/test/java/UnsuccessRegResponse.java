public class UnsuccessRegResponse {
    private String error;

    public UnsuccessRegResponse(String error) {
        this.error = error;
    }

    public UnsuccessRegResponse(){}

    public String getError() {
        return error;
    }
}
