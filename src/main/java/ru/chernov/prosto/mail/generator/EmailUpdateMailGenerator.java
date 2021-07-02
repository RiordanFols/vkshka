package ru.chernov.prosto.mail.generator;

import org.springframework.stereotype.Component;
import ru.chernov.prosto.mail.MailInfo;

/**
 * @author Pavel Chernov
 */
@Component("2")
public class EmailUpdateMailGenerator implements MailGenerator{

    @Override
    public String generate(MailInfo mailInfo) {
        return String.format("Для изменения почты вашего аккаунта перейдите по следующей ссылке: %s",
                "localhost:8080/activation/" + mailInfo.getUser().getActivationCode());
    }

    @Override
    public String getSubject() {
        return "Смена почты аккаунта";
    }
}
