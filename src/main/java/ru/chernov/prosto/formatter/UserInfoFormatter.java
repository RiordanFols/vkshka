package ru.chernov.prosto.formatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.chernov.prosto.domain.entity.User;
import ru.chernov.prosto.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;

/**
 * @author Pavel Chernov
 */
@Component
@SuppressWarnings("FieldCanBeLocal")
public class UserInfoFormatter {

    private final String VERB_MALE = "был";
    private final String VERB_FEMALE = "была";
    private final String VERB_UNDEFINED = "был(а)";

    private final String ONLINE_MESSAGE = "онлайн";
    private final String MINUTES_AGO_MESSAGE =" в сети %s минут назад";
    private final String HOURS_AGO_MESSAGE = " в сети %s часов назад";
    private final String DAYS_AGO_MESSAGE = " в сети %s дней назад";
    private final String LAST_DATE_MESSAGE = " в сети %s";

    @Value("${session.threshold.seconds}")
    private int secondsThreshold;

    @Value("${session.threshold.minutes}")
    private int minutesThreshold;

    @Value("${session.threshold.hours}")
    private int hoursThreshold;

    @Value("${session.threshold.days}")
    private int daysThreshold;

    public String formatLastOnlineString(User user) {
        var now = LocalDateTime.now();
        var lastOnline = user.getLastOnline();

        // сейчас онлайн
        int secondsAgo = (int)(now.toEpochSecond(ZoneOffset.UTC) - lastOnline.toEpochSecond(ZoneOffset.UTC));
        if (secondsAgo < secondsThreshold)
            return ONLINE_MESSAGE;

        // слова 'был' / 'была'
        var genderVerb = switch (user.getGender()) {
            case MALE -> VERB_MALE;
            case FEMALE -> VERB_FEMALE;
            case UNDEFINED -> VERB_UNDEFINED;
        };

        // был в сети n минут назад
        int minutesAgo = secondsAgo / 60;
        if (minutesAgo < minutesThreshold)
            return genderVerb + String.format(MINUTES_AGO_MESSAGE, minutesAgo);

        // был в сети n часов назад
        int hoursAgo = minutesAgo / 60;
        if (hoursAgo < hoursThreshold)
            return genderVerb + String.format(HOURS_AGO_MESSAGE, hoursAgo);

        // был в сети n часов назад
        int daysAgo = hoursAgo / 24;
        if (daysAgo < daysThreshold)
            return genderVerb + String.format(DAYS_AGO_MESSAGE, daysAgo);

        // показ полной даты последнего онлайна
        return genderVerb + String.format(LAST_DATE_MESSAGE, DateUtils.formatDate(lastOnline));
    }

    public String formatBirthdayString(User user) {
        return user.getBirthday() != null ? DateUtils.formatDate(user.getBirthday()) : "";
    }

    public String formatAge(User user) {
        if (user.getBirthday() == null)
            return "";

        LocalDate now = LocalDate.now();
        Period period = Period.between(user.getBirthday(), now);
        return String.valueOf(period.getYears());
    }
}
