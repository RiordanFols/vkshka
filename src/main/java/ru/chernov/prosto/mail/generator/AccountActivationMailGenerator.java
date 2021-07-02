package ru.chernov.prosto.mail.generator;

import org.springframework.stereotype.Component;
import ru.chernov.prosto.mail.MailInfo;

/**
 * @author Pavel Chernov
 */
@Component("1")
public class AccountActivationMailGenerator implements MailGenerator {

    @Override
    public String generate(MailInfo mailInfo) {
        return String.format("Добро пожаловать на наш сайт! Чтобы активировать свой аккаунт перейдите по ссылке: %s.",
                "localhost:8080/activation/" + mailInfo.getUser().getActivationCode());
    }

    @Override
    public String getSubject() {
        return "Активация аккаунта";
    }
}
