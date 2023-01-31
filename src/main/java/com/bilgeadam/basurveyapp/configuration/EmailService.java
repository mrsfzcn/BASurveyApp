package com.bilgeadam.basurveyapp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

//    private final JavaMailSender javaMailSender;
//
//    private static final String FROM = "hrmsapplicationteam1@gmail.com";
//    private static final String SUBJECT = "Şifremi unuttum! HR Manager";
//    private static final String PASSWORD_RESET_HTML_BODY = "<h1>Şifre değiştirme talebiniz alınmıştır</h1><a href='https://hrmanagerapp.azurewebsites.net/#/sifreolustur/$tokenValue'>Buraya tıklayarak şifrenizi değiştirebilirsiniz!</a><br/><br/>Bizi tercih ettiğiniz için teşekkür ederiz!";

    public void sendSurveyMail(Map<String,String> mailTokenMap) /**throws MessagingException*/ {

//        MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), "utf-8");
//
//        helper.setText(PASSWORD_RESET_HTML_BODY.replace("$tokenValue", token), true);
//        helper.setTo(email);
//        helper.setSubject(SUBJECT);
//        helper.setFrom(FROM);
//
//        javaMailSender.send(helper.getMimeMessage());

        // Recommended to use as below...!

//        MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), "utf-8");
//        for(String mailToken : mailTokenMap.keySet()){
//            helper.setText(PASSWORD_RESET_HTML_BODY.replace("$tokenValue", mailTokenMap.get(mailToken)), true);
//            helper.setTo(mailToken);
//            helper.setSubject(SUBJECT);
//            helper.setFrom(FROM);
//            javaMailSender.send(helper.getMimeMessage());
//        }
    }
}
