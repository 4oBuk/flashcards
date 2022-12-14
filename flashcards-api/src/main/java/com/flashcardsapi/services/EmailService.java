package com.flashcardsapi.services;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.flashcardsapi.entities.User;
import com.flashcardsapi.entities.VerificationToken;
import com.flashcardsapi.repositories.VerificationTokenRepository;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class EmailService {
    private static final int LETTER_VALIDITY_DAYS = 1;
    private VerificationTokenRepository tokenRepository;
    // private final JavaMailSender mailSender;

    public VerificationToken getToken(String token) {
        return tokenRepository.findByToken(token).orElse(null);
    }

    public boolean verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);
        if (verificationToken == null || verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public void sendVerificationLetter(User user) {
        VerificationToken verificationToken = createTokenByUser(user);
        tokenRepository.save(verificationToken);
        // todo:get letter from message builder and send it
    }

    public void sendEmailUpdateLetter(User user) {
        VerificationToken verificationToken = createTokenByUser(user);
        tokenRepository.save(verificationToken);
        // todo: I can delete this method and use only one
        // todo: get letter from message builder and send it
    }

    private VerificationToken createTokenByUser(User user) {
        VerificationToken verificationToken = new VerificationToken();
        String token = UUID.randomUUID().toString();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setExpiresAt(LocalDateTime.now().plusDays(LETTER_VALIDITY_DAYS));
        return verificationToken;
    }

    private void sendEmail(String email, String to, String subject) {
        // try {
        // javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
        // MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
        // "utf-8");
        // mimeMessageHelper.setText(email, true);
        // mimeMessageHelper.setTo(to);
        // mimeMessageHelper.setSubject(subject);
        // mimeMessageHelper.setFrom("");// todo: add email
        // mailSender.send(mimeMessage);
        // } catch (MessagingException e) {

        // }
    }
}
