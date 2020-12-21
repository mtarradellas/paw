package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.models.constants.MailArg;
import ar.edu.itba.paw.models.constants.MailType;
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

    @Async
    public void sendMail(String recipient, String recipientLocale, Map<MailArg, Object> arguments, MailType mailType) {

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

    /** TODO methond not used */
    // private String getMailTemplateName(MailType mailType){
    //     return mailType.getName() + ".ftl";
    // }

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
