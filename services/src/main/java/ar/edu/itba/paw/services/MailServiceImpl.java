package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.models.constants.MailArg;
import ar.edu.itba.paw.models.constants.MailType;
import ar.edu.itba.paw.models.constants.MailUrl;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private MessageSource messageSource;

    private void sendMail(String recipient, String recipientLocale, Map<MailArg, Object> arguments, MailType mailType) {

        MimeMessagePreparator preparator = mimeMessage -> {

            Locale locale = getLocaleForMail(recipientLocale);
            MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource,locale);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(recipient);
            helper.setSubject(messageSourceAccessor.getMessage(mailType.getName() + ".subject"));

            VelocityContext context = new VelocityContext();

            for(MailArg key : arguments.keySet()){
                context.put(key.getName(), arguments.get(key));
            }
            context.put("msg", messageSourceAccessor);

            StringWriter stringWriter = new StringWriter();
            velocityEngine.mergeTemplate("mail_templates/" + mailType.getName() + ".vm",
                    "UTF-8", context, stringWriter);
            String text = stringWriter.toString();


            helper.setText(text,true);
        };

        /* TODO try catch for exception handling */
        mailSender.send(preparator);
    }

    @Async
    public void sendActivateAccountMail(String recipient, String recipientLocale, String token, String username){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.TOKEN, token);
        arguments.put(MailArg.USERNAME, username);

        sendMail(recipient, recipientLocale, arguments, MailType.ACTIVATE_ACCOUNT);
    }

    @Async
    public void sendPetSoldMail(String recipient, String recipientLocale, String petURL, String petName, String ownerURL, String ownerName, String username){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETURL, petURL);
        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.OWNERURL, ownerURL);
        arguments.put(MailArg.OWNERNAME, ownerURL);
        arguments.put(MailArg.USERNAME, username);

        sendMail(recipient, recipientLocale, arguments, MailType.PET_SOLD);
    }

    @Async
    public void sendQuestionAnswerMail(String recipient, String recipientLocale, String petURL, String petName, String username, String question, String answer){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETURL, petURL);
        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.USERNAME, username); // User who answered the question (pet owner)
        arguments.put(MailArg.QUESTION, question);
        arguments.put(MailArg.ANSWER, answer);

        sendMail(recipient, recipientLocale, arguments, MailType.QUESTION_ANSWER);
    }

    @Async
    public void sendQuestionAskMail(String recipient, String recipientLocale, String petURL, String petName, String username, String question){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETURL, petURL);
        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.USERNAME, username); // User who asked the question
        arguments.put(MailArg.QUESTION, question);

        sendMail(recipient, recipientLocale, arguments, MailType.QUESTION_ASK);
    }

    @Async
    public void sendRequestMail(String recipient, String recipientLocale, String petName, String ownerName, String requestURL){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.OWNERNAME, ownerName);
        arguments.put(MailArg.REQUESTURL, requestURL);

        sendMail(recipient, recipientLocale, arguments, MailType.REQUEST);
    }

    @Async
    public void sendRequestCancelMail(String recipient, String recipientLocale, String petName, String requestURL, String ownerName){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.REQUESTURL, requestURL);
        arguments.put(MailArg.OWNERNAME, ownerName);

        sendMail(recipient, recipientLocale, arguments, MailType.REQUEST_CANCEL);
    }

    @Async
    public void sendRequestRecoverMail(String recipient, String recipientLocale, String petName, String requestURL, String ownerName){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.PETNAME, petName);
        arguments.put(MailArg.REQUESTURL, requestURL);
        arguments.put(MailArg.OWNERNAME, ownerName);

        sendMail(recipient, recipientLocale, arguments, MailType.REQUEST_RECOVER);
    }

    @Async
    public void sendRequestRejectMail(String recipient, String recipientLocale, String url, String petName, String ownerName){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.URL, url);
        arguments.put(MailArg.PETNAME,petName);
        arguments.put(MailArg.OWNERNAME, ownerName);

        sendMail(recipient, recipientLocale, arguments, MailType.REQUEST_REJECT);
    }

    @Async
    public void sendResetPasswordMail(String recipient, String recipientLocale, String token, String username){
        Map<MailArg, Object> arguments = new HashMap<>();

        arguments.put(MailArg.TOKEN, token);
        arguments.put(MailArg.USERNAME,username);

        sendMail(recipient, recipientLocale, arguments, MailType.RESET_PASSWORD);
    }

    private Locale getLocaleForMail(String recipientLocale){
        if(recipientLocale == null){
            return new Locale("es");
        }
        String language = recipientLocale.split("_")[0];
        if(language != null && (language.equals("en") || language.equals("es")) ){
            return new Locale(language);
        }else{
            return new Locale("es");
        }
    }

}
