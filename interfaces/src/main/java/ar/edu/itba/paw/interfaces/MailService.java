package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.constants.MailType;

import java.util.Map;

public interface MailService {
    void sendMail(String recipient, Map<String, Object> arguments, MailType mailType);
}
