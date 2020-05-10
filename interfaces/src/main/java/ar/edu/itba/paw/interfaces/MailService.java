package ar.edu.itba.paw.interfaces;

import java.io.StringWriter;

public interface MailService {
    void sendRequestPetMail(String recipient, String language);
}
