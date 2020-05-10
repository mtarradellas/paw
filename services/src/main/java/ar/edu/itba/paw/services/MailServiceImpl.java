package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.Map;

import ar.edu.itba.paw.interfaces.MailService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("mailService")
@Configuration
@EnableAsync
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    MessageSource messageSource;

    @Async
    public void sendMail(String recipient, Map<String, Object> arguments, String mailType){
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);

        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(recipient);
            helper.setSubject(messageSourceAccessor.getMessage(mailType + "Subject"));
            VelocityContext context = new VelocityContext();
            context.put("bodyMessages", messageSourceAccessor);
            for(String key : arguments.keySet()){
                context.put(key,arguments.get(key));
            }
            StringWriter stringWriter = new StringWriter();
            velocityEngine.mergeTemplate("templates/" + mailType + ".vm", "UTF-8", context, stringWriter);
            String text = stringWriter.toString();

            helper.setText(text,true);
        };

        mailSender.send(preparator);
    }

}
