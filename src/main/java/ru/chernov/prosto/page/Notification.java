package ru.chernov.prosto.page;

/**
 * @author Pavel Chernov
 */
public enum Notification {
    REGISTRATION_SUCCESSFUL("Регистрация прошла успешно. Введите свои логин и пароль"),
    PASSWORD_UPDATE_SUCCESSFUL("Пароль успешно изменен"),
    DATA_UPDATE_SUCCESSFUL("Данные сохранены");

    private final String description;

    Notification(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
