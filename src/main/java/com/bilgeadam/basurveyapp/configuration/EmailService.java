package com.bilgeadam.basurveyapp.configuration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SimpleMailMessage preConfiguredMessage;

    private static final String FROM = "stajnotify@bilgeadamakademi.com";
    private static final String SUBJECT = "BilgeAdam Değerlendirme Anketi";


    private static final String PASSWORD_RESET_HTML_BODY =
            "<h1>BilgeAdam Değerlendirme Anketi</h1>" +
                    "<a href='http://localhost:5173/anket-doldurma-sayfası?Value=$tokenValue'>" +
                    "Ankete gitmek için tıklayın!" +
                    "</a>" +
                    "<br/><br/>" +
                    "Bizi tercih ettiğiniz için teşekkür ederiz!";


    public void sendSurveyMail(Map<String,String> mailTokenMap) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        for (Map.Entry<String, String> entry : mailTokenMap.entrySet()) {
            helper.setText(PASSWORD_RESET_HTML_BODY.replace("$tokenValue", entry.getValue()), true);
            helper.setTo(entry.getKey());
            helper.setSubject(SUBJECT);
            helper.setFrom(FROM);
            javaMailSender.send(helper.getMimeMessage());
        }

        for (Map.Entry<String, String> entry : mailTokenMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

    }

    public void sendSurveyMail(String email, String jwtToken) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(PASSWORD_RESET_HTML_BODY.replace("$tokenValue", jwtToken), true);
        helper.setTo(email);
        helper.setSubject(SUBJECT);
        helper.setFrom(FROM);

        javaMailSender.send(helper.getMimeMessage());
    }
}
