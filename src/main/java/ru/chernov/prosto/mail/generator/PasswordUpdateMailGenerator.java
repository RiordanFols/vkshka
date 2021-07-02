package ru.chernov.prosto.mail.generator;

import org.springframework.stereotype.Component;
import ru.chernov.prosto.mail.MailInfo;

/**
 * @author Pavel Chernov
 */
@Component("3")
public class PasswordUpdateMailGenerator implements MailGenerator {

    @Override
    public String generate(MailInfo mailInfo) {
        return "Пароль от вашей учетной записи был изменен." +
                " Если это были не вы, то свяжитесь с нашей службой поддержки.";
    }

    @Override
    public String getSubject() {
        return "Изменение пароля учетной записи";
    }
}
