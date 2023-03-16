package mii.bsi.apiportal.utils;

import mii.bsi.apiportal.constant.Params;
import mii.bsi.apiportal.domain.BsmApiConfig;
import mii.bsi.apiportal.domain.SystemNotification;
import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.model.ConfigEmail;
import mii.bsi.apiportal.domain.model.SendEmail;
import mii.bsi.apiportal.repository.BsmApiConfigRepository;
import mii.bsi.apiportal.repository.SystemNotificationRepository;
import mii.bsi.apiportal.service.ParamsService;
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
import java.util.*;

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

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    private EncryptUtility encryptUtility;
    @Autowired
    private BsmApiConfigRepository configRepository;
    @Autowired
    private ParamsService paramsService;
    @Autowired
    private SystemNotificationRepository notificationRepository;

    public String sendEmailVerification(User user, String token){
        SystemNotification notification;
        try {
            ConfigEmail configEmail = getEmailConfig();
            final BsmApiConfig baseUrl = configRepository.findByKeynameAndKeygroup("base.url", "URL");
            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("emailVerificationUrl", baseUrl.getValue()+"/verification-email?token=" + token+"&uid=" + encUid);
            model.put("email", user.getEmail());

            String content = getContentFromTemplate(model, "id", "emailVerification.vm");
            SendEmail sendEmail = new SendEmail("Email Verification", configEmail.getUsername().getValue(),user.getEmail(), content);

            notification = generateNotification(sendEmail, user);
            System.out.println(notification.getNotifMessage());
            notification = notificationRepository.save(notification);

            sendEmailNow(configEmail, sendEmail);

            notification.setSuccess(true);
            notificationRepository.save(notification);

            return null;

        } catch (TemplateException | MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String sendForgetPassword(User user, String token){
        SystemNotification notification;
        try {
            ConfigEmail configEmail = getEmailConfig();
            final BsmApiConfig baseUrl = configRepository.findByKeynameAndKeygroup("base.url", "URL");
            final String encUid = encryptUtility.encryptAES(user.getId(), Params.PASS_KEY);

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", user.getFirstName()+ " " + user.getLastName());
            model.put("resetPasswordUrl", baseUrl.getValue()+"/reset-password?token=" + token+"&id="+encUid);
            model.put("email", user.getEmail());

            String content = getContentFromTemplate(model, "id", "resetPassword.vm");

            SendEmail sendEmail = new SendEmail("Forget Password",configEmail.getUsername().getValue(), user.getEmail(), content);
            notification = generateNotification(sendEmail, user);
            System.out.println(notification.getNotifMessage());
            notification = notificationRepository.save(notification);

            sendEmailNow(configEmail, sendEmail);

            notification.setSuccess(true);
            notificationRepository.save(notification);

            return null;

        } catch (TemplateException | MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String sendEmailOTPVerification(String email, String nama, String token){

        try {
            ConfigEmail configEmail = getEmailConfig();

//            JavaMailSenderImpl mailSender = getMailSender(configEmail.getHost().getValue(),
//                    Integer.parseInt(configEmail.getPort().getValue()),
//                    configEmail.getUsername().getValue(),  configEmail.getPassword().getValue());
//            mailSender.setJavaMailProperties(getProperties());

//            String from = configEmail.getUsername().getValue();
//            String subject = "OTP Email Verification";

            Map<String, Object> model = new HashMap<>();
            model.put("fullName", nama);
            model.put("kode", token);
            model.put("email", email);
            String content = getContentFromTemplate(model, "id", "otpEmailVerification.vm");

            SendEmail sendEmail = new SendEmail("OTP Email Verification", configEmail.getUsername().getValue(), email, content);

//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);

//            helper.setSubject(subject);
//            helper.setFrom(from);
//            helper.setTo(email);
//            helper.setText(content, true);
//            mailSender.send(helper.getMimeMessage());
//
//            System.out.println("Content : "+ content );

            return null;

        } catch (TemplateException | MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendEmailNow(ConfigEmail configEmail, SendEmail sendEmail){

        try {
            JavaMailSenderImpl mailSender = getMailSender(configEmail.getHost().getValue(),
                    Integer.parseInt(configEmail.getPort().getValue()),
                    configEmail.getUsername().getValue(),  configEmail.getPassword().getValue());

            mailSender.setJavaMailProperties(getProperties());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setSubject(sendEmail.getSubject());
            helper.setFrom(sendEmail.getFrom());
            helper.setTo(sendEmail.getTo());
            helper.setText(sendEmail.getText(), true);
            mailSender.send(helper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }



    }

    private ConfigEmail getEmailConfig(){
        List<BsmApiConfig> listConfig = paramsService.getParamsByKeyGroup("EMAIL");

        final BsmApiConfig paramHost =  listConfig.stream()
                .filter(parameterConfig ->  parameterConfig.getKeyname().equals(host)).findAny().orElse(null);
        final BsmApiConfig paramPort =  listConfig.stream()
                .filter(parameterConfig ->  parameterConfig.getKeyname().equals(port)).findAny().orElse(null);
        final BsmApiConfig paramUsername =  listConfig.stream()
                .filter(parameterConfig ->  parameterConfig.getKeyname().equals(username)).findAny().orElse(null);
        final BsmApiConfig paramPassword =  listConfig.stream()
                .filter(parameterConfig ->  parameterConfig.getKeyname().equals(password)).findAny().orElse(null);

        return new ConfigEmail(paramHost, paramPort, paramUsername, paramPassword);
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

    private SystemNotification generateNotification(SendEmail sendEmail, User user){
        SystemNotification notification = new SystemNotification();
        notification.setMediaType("EMAIL");
        notification.setNotifDate(new Date());
        notification.setNotifSubject(sendEmail.getSubject());
        notification.setNotifMessage(sendEmail.getText());
        notification.setNotifType(sendEmail.getSubject());
        notification.setRetry(0);
        notification.setErrorCode("00");
        notification.setMediaAddress(user.getEmail());
        notification.setSuccess(false);
        notification.setMitraId(user.getId());
        notification.setMitraName(user.getFirstName()+ " "+ user.getLastName());
        return notification;
    }

    private String getContentFromTemplate(Map <String, Object>model, String lang, String template)   throws IOException, TemplateException, MessagingException {
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
