package ru.chernov.prosto.mail.generator;

import ru.chernov.prosto.mail.MailInfo;

/**
 * @author Pavel Chernov
 */
public interface MailGenerator {
    String generate(MailInfo mailInfo);

    String getSubject();
}
