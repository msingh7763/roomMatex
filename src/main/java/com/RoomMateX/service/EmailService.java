package com.RoomMateX.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender sender;

    public void sendHtml(String to, String subject, String html){

        try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message,true,"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html,true); // true = HTML

            sender.send(message);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
