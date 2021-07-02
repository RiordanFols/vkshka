package ru.chernov.prosto.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import ru.chernov.prosto.mail.generator.MailGenerator;

import java.util.Map;

/**
 * @author Pavel Chernov
 */
@Component
public class MailManager {

    @Value("${spring.mail.username}")
    private String username;

    private final Map<String, MailGenerator> mailGeneratorMap;
    private final MailSender mailSender;

    @Autowired
    public MailManager(Map<String, MailGenerator> mailGeneratorMap, MailSender mailSender) {
        this.mailGeneratorMap = mailGeneratorMap;
        this.mailSender = mailSender;
    }

    public void send(MailInfo mailInfo) {
        MailGenerator mailGenerator = mailGeneratorMap.get(mailInfo.getCode());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(mailInfo.getUser().getEmail());
        mailMessage.setText(mailGenerator.generate(mailInfo));
        mailMessage.setSubject(mailGenerator.getSubject());

        // todo: error cause wrong gmail settings
//        mailSender.send(mailMessage);
    }
}
