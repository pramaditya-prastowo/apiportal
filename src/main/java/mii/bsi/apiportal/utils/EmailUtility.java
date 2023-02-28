package mii.bsi.apiportal.utils;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class EmailUtility {

//    private static final String host = "smtp.gmail.com";
//    private static final int port = 587;
//    private static final String username = "uswa.khasanah0212@gmail.com";
//    private static final String password = "ywfthuladfdbypoe";

    @Value("${email.host}")
    private String host;
    @Value("${email.port}")
    private String port;
    @Value("${email.username}")
    private String username;
    @Value("${email.password}")
    private String password;
//    private static final String host = "email.host";
//    private static final String port = "email.port";
//    private static final String username = "email.user";
//    private static final String password = "email.pass";

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    @Autowired
    private EncryptUtility encryptUtility;

    @Autowired
    private BsmApiConfigRepository configRepository;

    public String sendEmailVerification(User user, String token){

        try {
            List<BsmApiConfig> listConfig = configRepository.findByKeygroup("EMAIL");

            final BsmApiConfig paramHost =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(host)).findAny().orElse(null);
            final BsmApiConfig paramPort =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(port)).findAny().orElse(null);
            final BsmApiConfig paramUsername =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(username)).findAny().orElse(null);
            final BsmApiConfig paramPassword =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(password)).findAny().orElse(null);

            final BsmApiConfig baseUrl = configRepository.findByKeynameAndKeygroup("base.url", "URL");

            JavaMailSenderImpl mailSender = getMailSender(paramHost.getValue(), Integer.parseInt(paramPort.getValue()),
                    paramUsername.getValue(), paramPassword.getValue());

            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);
            mailSender.setJavaMailProperties(getProperties());

            String from = paramUsername.getValue();
            String subject = "Email Verification";

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("emailVerificationUrl", baseUrl.getValue()+"/verification-email?token=" + token+"&uid=" + encUid);
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


        try {
            List<BsmApiConfig> listConfig = configRepository.findByKeygroup("EMAIL");

            final BsmApiConfig paramHost =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(host)).findAny().orElse(null);
            final BsmApiConfig paramPort =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(port)).findAny().orElse(null);
            final BsmApiConfig paramUsername =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(username)).findAny().orElse(null);
            final BsmApiConfig paramPassword =  listConfig.stream()
                    .filter(parameterConfig ->  parameterConfig.getKeyname().equals(password)).findAny().orElse(null);

            final BsmApiConfig baseUrl = configRepository.findByKeynameAndKeygroup("base.url", "URL");

            JavaMailSenderImpl mailSender = getMailSender(paramHost.getValue(), Integer.parseInt(paramPort.getValue()),
                    paramUsername.getValue(), paramPassword.getValue());
            mailSender.setJavaMailProperties(getProperties());
            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);

            String from = username;
            String subject = "Forget Password";

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("resetPasswordUrl", baseUrl.getValue()+"/reset-password?token=" + token+"&id="+encUid);
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
    private JavaMailSenderImpl getMailSender(String host, int port, String username,String pass){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(pass);
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
