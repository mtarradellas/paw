package ar.edu.itba.paw.webapp.dto;

public class ErrorDto {

    private int code; 
    private String message;

    public ErrorDto() {
        //
    }

    public ErrorDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
