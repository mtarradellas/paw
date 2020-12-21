package ar.edu.itba.paw.models.constants;

public enum MailUrl {
    ACTIVATE_AC("activate-account/"),
    RESET_PASS("password-reset/"),
    PET("pet/"),
    REQUESTS("requests/"),
    INTERESTS("interests/");

    private final String url;

    MailUrl(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
