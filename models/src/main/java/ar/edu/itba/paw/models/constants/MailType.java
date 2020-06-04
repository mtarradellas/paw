package ar.edu.itba.paw.models.constants;

public enum MailType {
    REQUEST("request"),
    REQUEST_ACCEPT("request_accept"),
    REQUEST_REJECT("request_reject"),
    REQUEST_CANCEL("request_cancel"),
    REQUEST_RECOVER("request_recover"),
    ACTIVATE_ACCOUNT("activate_account"),
    RESET_PASSWORD("reset_password");


    private final String type;

    MailType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
