package ar.edu.itba.paw.models.constants;

public enum MailType {
    REQUEST("request"),
    REQUEST_ACCEPT("request_accept"),
    REQUEST_REJECT("request_reject"),
    REQUEST_CANCEL("request_cancel"),
    REQUEST_RECOVER("request_recover"),
    ACTIVATE_ACCOUNT("activate_account"),
    RESET_PASSWORD("reset_password"),
    PET_SOLD("pet_sold");

    private final String name;

    MailType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
