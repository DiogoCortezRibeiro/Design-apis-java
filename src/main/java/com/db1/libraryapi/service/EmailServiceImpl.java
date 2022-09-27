package com.db1.libraryapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private String remetente = "mail@library-api.com";
    @Override
    public void sendEmails(List<String> emails, String message) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setSubject("Livro com emprestimo atrasado");
        mensagem.setText(message);
        mensagem.setTo(emails.toArray(new String[emails.size()]));

        javaMailSender.send(mensagem);
    }
}
