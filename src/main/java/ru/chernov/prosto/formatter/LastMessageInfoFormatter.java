package ru.chernov.prosto.formatter;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.chernov.prosto.domain.entity.Message;

/**
 * @author Pavel Chernov
 */
@Component
@Getter
public class LastMessageInfoFormatter {

    private final static int MAX_LENGTH = 24;
    private final static String PHOTO = "Фотография";
    private final static String USER_IS_AUTHOR = "Вы:";
    private final static String LIMITED_MESSAGE_ENDING = "...";

    public String formatLastMessageInfo(Message message) {

        if (!message.getImgFilenames().isEmpty())
            return PHOTO;

        if (message.getText().length() >= MAX_LENGTH) {
            return message.getText().substring(0, MAX_LENGTH) + LIMITED_MESSAGE_ENDING;
        } else {
            return message.getText();
        }
    }

    public String formatAuthor(Message message, long userId) {
        return message.getAuthor().getId() == userId ? USER_IS_AUTHOR : null;
    }
}
