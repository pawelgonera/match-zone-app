package pl.poul12.matchzone.util;

public class CustomErrorResponse {

    private String errorMessage;

    public CustomErrorResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /*@Override
    public String toString() {
        return "CustomErrorResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }*/
}
