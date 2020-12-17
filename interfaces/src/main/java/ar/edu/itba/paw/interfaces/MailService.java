package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.constants.MailArg;
import ar.edu.itba.paw.models.constants.MailType;

import java.util.Map;

public interface MailService {
    void sendMail(String recipient, String recipientLocale, Map<MailArg, Object> arguments, MailType mailType);
}
