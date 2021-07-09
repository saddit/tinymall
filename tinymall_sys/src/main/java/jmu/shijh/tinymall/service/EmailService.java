package jmu.shijh.tinymall.service;

import org.springframework.mail.MailException;

public interface EmailService {
    void sendSimpleEmail(String title, String content, String... to);
}
