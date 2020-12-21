package ar.edu.itba.paw.models.constants;

public enum MailArg {
    PETURL("petURL"),
    PETNAME("petName"),
    OWNERURL("ownerURL"),
    OWNERNAME("ownerName"),
    USERURL("userURL"),
    USERNAME("userName"),
    QUESTION("question"),
    ANSWER("answer"),
    REQUESTURL("requestURL"),
    REQUEST("request"),
    URL("URL"),
    TOKEN("URLToken");

    private final String name;

    MailArg(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
