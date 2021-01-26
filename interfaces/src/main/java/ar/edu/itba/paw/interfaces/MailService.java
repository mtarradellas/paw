package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.constants.MailArg;
import ar.edu.itba.paw.models.constants.MailType;

import java.util.Map;

public interface MailService {
    void sendActivateAccountMail(String recipient, String recipientLocale, String token, String username);
    void sendPetSoldMail(String recipient, String recipientLocale, String petURL, String petName, String ownerURL, String ownerName, String username);
    void sendQuestionAnswerMail(String recipient, String recipientLocale, String petURL, String petName, String username, String question, String answer);
    void sendQuestionAskMail(String recipient, String recipientLocale, String petURL, String petName, String username, String question);
    void sendRequestMail(String recipient, String recipientLocale, String petName, String ownerName, String requestURL);
    void sendRequestCancelMail(String recipient, String recipientLocale, String petName, String requestURL, String ownerName);
    void sendRequestRecoverMail(String recipient, String recipientLocale, String petName, String requestURL, String ownerName);
    void sendRequestRejectMail(String recipient, String recipientLocale, String url, String petName, String ownerName);
    void sendResetPasswordMail(String recipient, String recipientLocale, String token, String username);
}
