package ar.edu.itba.paw.interfaces;

public interface MailService {
    void sendMail(String recipient, String subject, String body);
    void sendMailWithAttachment(String to, String subject, String body, String fileToAttach);
}
