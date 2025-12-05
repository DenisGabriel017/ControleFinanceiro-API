package com.dnsotware.appfinanceiro_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("%{spring.email.username}")
    private String remetente;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmailTexto(String destinatario, String assunto, String mensagem){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            simpleMailMessage.setTo(destinatario);
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(mensagem);

            javaMailSender.send(simpleMailMessage);
            System.out.println("Email enviado com sucesso para: " + destinatario);
        }catch (Exception e){
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

}
