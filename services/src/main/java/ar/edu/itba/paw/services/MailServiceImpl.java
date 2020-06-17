package ar.edu.itba.paw.services;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.models.constants.MailType;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sun.util.locale.LocaleUtils;

@Service("mailService")
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    Configuration freemarkerConfiguration;

    @Autowired
    MessageSource messageSource;

    @Async
    public void sendMail(String recipient, String recipientLocale, Map<String, Object> arguments, MailType mailType){


        MimeMessagePreparator preparator = mimeMessage -> {

            Locale locale = getLocaleForMail(recipientLocale);
            MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource,locale);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(recipient);
            helper.setSubject(messageSourceAccessor.getMessage(mailType.getName() + ".subject"));

            String templateName = getMailTemplateName(mailType);
            if (templateName.equals("")) {
                LOGGER.warn("Could not find FreeMarker template for mail type");
                return;
            }
            arguments.put("msg", messageSourceAccessor);
            String text = geFreeMarkerTemplateContent(arguments, templateName);

            helper.setText(text,true);
        };

        /* TODO try catch for exception handling */
        mailSender.send(preparator);
    }

    private String geFreeMarkerTemplateContent(Map<String, Object> model, String templateName){
        StringBuilder content = new StringBuilder();
        try{
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate(templateName), model));
            return content.toString();
        }catch(Exception e){
            LOGGER.warn("Exception occured while processing fmtemplate " + templateName);
        }
        return "";
    }

    private String getMailTemplateName(MailType mailType){
        return mailType.getName() + ".ftl";
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
