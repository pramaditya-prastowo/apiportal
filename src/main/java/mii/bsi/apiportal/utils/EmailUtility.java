package mii.bsi.apiportal.utils;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class EmailUtility {

    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static final String USERNAME = "uswa.khasanah0212@gmail.com";
    private static final String PASSWORD = "ywfthuladfdbypoe";

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    @Autowired
    private EncryptUtility encryptUtility;

    public String sendEmailVerification(User user, String token){
        JavaMailSenderImpl mailSender = getMailSender();

        try {
            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);
            mailSender.setJavaMailProperties(getProperties());

            String from = USERNAME;
            String subject = "Email Verification";

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("emailVerificationUrl", "http://localhost:4200/verification-email?token=" + token+"&uid=" + encUid);
            model.put("email", user.getEmail());
            String content = geContentFromTemplate(model, "id", "emailVerification.vm");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());

            System.out.println("Content : "+ content );

            return null;

        } catch (TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String sendForgetPassword(User user, String token){
        JavaMailSenderImpl mailSender = getMailSender();

        try {

            mailSender.setJavaMailProperties(getProperties());
            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);

            String from = USERNAME;
            String subject = "Forget Password";

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("resetPasswordUrl", "http://localhost:4200/reset-password?token=" + token+"&id="+encUid);
            model.put("email", user.getEmail());
            String content = geContentFromTemplate(model, "id", "resetPassword.vm");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());

            System.out.println("Content : "+ content );

            return null;

        } catch (TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private JavaMailSenderImpl getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);
        return mailSender;
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        return properties;
    }

    private String geContentFromTemplate(Map <String, Object>model, String lang, String template)   throws IOException, TemplateException, MessagingException {
        Template freemarkerTemplate;
        freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                .getTemplate(template);
//        if(lang.equals("en")) {
//            freemarkerTemplate = freemarkerConfigurer.getConfiguration()
//                    .getTemplate("emailVerification.vm");
//        }else {
//            freemarkerTemplate = freemarkerConfigurer.getConfiguration()
//                    .getTemplate("resetPasswordToken_ID.vm");
//        }
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
        return htmlBody;
    }
}
