package ar.edu.itba.paw.interfaces;

import java.io.StringWriter;
import java.util.Map;

public interface MailService {
    void sendMail(String recipient, Map<String, Object> arguments, String mailType);
}
