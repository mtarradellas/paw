package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.Map;

import ar.edu.itba.paw.interfaces.MailService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

//    private void sendMail(String recipient, String subject, StringWriter body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setTo(recipient);
////        message.setSubject(subject);
//        message.setText(body.toString());
//        mailSender.send(message);
//    }

    public void sendRequestPetMail(String recipient, String language){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        VelocityContext context = new VelocityContext();
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("templates/request_pet.vm", "UTF-8", context, stringWriter);
        String text = stringWriter.toString();

        context.put("value", "pingo");



        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setText(text, true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }


//        sendMail(recipient, subject, stringWriter);
    }

//          String subject;
//
//        if(language.equals("en_US")){
//            subject = "A user showed interest in one of your pets!";
//        }else{
//            subject = "¡Un usuario mostró interés en una de sus mascotas!";
//        }

}
