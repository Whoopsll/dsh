package com.nh.dsh.admin.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Resource
    JavaMailSender javaMailSender;
    public void sendMail(String from,String to,String subject,String text){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(from);//发送者
        smm.setTo(to);//收件人
        smm.setSubject(subject);//邮件主题
        smm.setText(text);//邮件内容
        javaMailSender.send(smm);//发送邮件
    }

}
