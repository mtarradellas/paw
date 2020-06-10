package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.models.constants.MailType;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    VelocityEngine velocityEngine;

    @Autowired
    MessageSource messageSource;

    @Async
    public void sendMail(String recipient, String recipientLocale, Map<String, Object> arguments, MailType mailType){
        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(recipient);
            helper.setSubject(messagesSelector(recipientLocale, mailType, arguments));
            VelocityContext context = new VelocityContext();

            for(String key : arguments.keySet()){
                context.put(key,arguments.get(key));
            }

            StringWriter stringWriter = new StringWriter();
            velocityEngine.mergeTemplate("templates/" + mailType.getType() + ".vm", "UTF-8", context, stringWriter);
            String text = stringWriter.toString();

            helper.setText(text,true);
        };

        /* TODO try catch for exception handling */
        mailSender.send(preparator);
    }

    private String messagesSelector(String locale, MailType mailType, Map<String, Object> arguments){
        switch(mailType){
            case ACTIVATE_ACCOUNT:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("welcomeMsg","¡Bienvenido a nuestro sitio!");
                    arguments.put("usernameMsg","Este es su nuevo nombre de usuario");
                    arguments.put("linkMsg","<br>Por favor vaya al siguiente link para activar su cuenta:<br>");
                    arguments.put("gotoSiteMsg","¡Vaya a nuestro sitio para comenzar a mirar mascotas!");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "Por favor, active su cuenta";
                }else if(locale.equals("en_US")){
                    arguments.put("welcomeMsg","Welcome to our site!");
                    arguments.put("usernameMsg","This is your new username");
                    arguments.put("linkMsg","<br>Please go to the link below to activate your account:<br>");
                    arguments.put("gotoSiteMsg","Go to our site to start looking for pets!");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "Please, activate your account";
                }
            case RESET_PASSWORD:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("linkMsg", "<br>Por favor vaya al siguiente link para resetearla:<br>");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "Resetee su contraseña";
                }else if(locale.equals("en_US")){
                    arguments.put("linkMsg", "<br>Please go to the link below to reset your password:<br>");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "Reset Your Password";
                }
            case REQUEST:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("requesterMsg", "Usuario que hizo la solicitud");
                    arguments.put("petMsg", "Mascota deseada");
                    arguments.put("requestMsg", "¡Haga click aquí para ir a aceptar o rechazar la solicitud!");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "¡Un usuario mostró interes en una de sus mascotas!";
                }else if(locale.equals("en_US")){
                    arguments.put("requesterMsg", "User that made the request");
                    arguments.put("petMsg", "Target Pet");
                    arguments.put("requestMsg", "Click here to go to accept or reject this request!");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "A user showed interest in one of your pets!";
                }
            case REQUEST_ACCEPT:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("actionMsg", "Su solicitud fue aceptada por");
                    arguments.put("petMsg", "Mascota deseada");
                    arguments.put("contactMsg", "Para iniciar el proceso de conseguir su mascota, contáctese con este mail");
                    arguments.put("mailMsg", "Para más información, contáctese con petsociety.contact@gmail.com");
                    arguments.put("gotoSiteMsg", "¡Haga click aquí para ir a nuestro sitio!");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "¡Genial! Su solicitud fue aceptada";
                }else if(locale.equals("en_US")){
                    arguments.put("actionMsg", "Your request was accepted by");
                    arguments.put("petMsg", "Target Pet");
                    arguments.put("contactMsg", "To begin the process for getting your new pet, please contact this mail");
                    arguments.put("mailMsg", "For more information, contact us at petsociety.contact@gmail.com");
                    arguments.put("gotoSiteMsg", "Click here to go to our site!");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "Hooray! Your request was accepted";
                }
            case REQUEST_REJECT:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("actionMsg", "Su solicitud fue rechazada por");
                    arguments.put("petMsg", "Mascota deseada");
                    arguments.put("mailMsg", "Para más información, contáctese con petsociety.contact@gmail.com");
                    arguments.put("gotoSiteMsg", "¡Haga click aquí para ir a nuestro sitio!");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "Lo sentimos, su solicitud fue rechazada";
                }else if(locale.equals("en_US")){
                    arguments.put("actionMsg", "Your request was rejected by");
                    arguments.put("petMsg", "Target Pet");
                    arguments.put("mailMsg", "For more information, contact us at petsociety.contact@gmail.com");
                    arguments.put("gotoSiteMsg", "Click here to go to our site!");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "We're sorry, your request was rejected";
                }
            case REQUEST_RECOVER:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("actionMsg", "La solicitud fue renovada por");
                    arguments.put("petMsg", "Mascota solicitada");
                    arguments.put("requestMsg", "¡Haga click aquí para ir a aceptar o rechazar la solicitud!");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "Un usuario ha renovado una solicitud de una mascota";
                }else if(locale.equals("en_US")){
                    arguments.put("actionMsg", "The request was recovered by");
                    arguments.put("petMsg", "Requested pet");
                    arguments.put("requestMsg", "¡Haga click aquí para ir a aceptar o rechazar la solicitud!");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "A user has recovered a request for a pet";
                }
            case REQUEST_CANCEL:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("actionMsg", "La solicitud fue cancelada por");
                    arguments.put("petMsg", "Mascota cancelada");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "Un usuario ha cancelado una solicitud de una mascota";
                }else if(locale.equals("en_US")){
                    arguments.put("actionMsg", "The request was canceled by");
                    arguments.put("petMsg", "Target Pet");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "A user has canceled a request for a pet";
                }
            case PET_SOLD:
                if(locale == null || locale.equals("es_AR")){
                    arguments.put("actionMsg", "Dueño original");
                    arguments.put("petMsg", "Mascota adoptada");
                    arguments.put("goodbyeMsg", "<br>Sinceramente,<br>El equipo de Pet Society.");
                    return "¡Felicitaciones! Se ha finalizado el trámite de adopción de una mascota";
                }else if(locale.equals("en_US")){
                    arguments.put("actionMsg", "Original owner");
                    arguments.put("petMsg", "Target Pet");
                    arguments.put("goodbyeMsg", "<br>Sincerely,<br>Pet Society Team.");
                    return "Hooray! The adoption process has been completed";
                }
        }
        return null;
    }
}
