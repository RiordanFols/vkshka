package ru.chernov.prosto.page;

/**
 * @author Pavel Chernov
 */
public enum Error {
    PASSWORDS_NOT_SAME("Пароли не совпадают"),
    USERNAME_IS_TAKEN("Пользователь с таким именем уже существует"),
    EMAIL_IS_TAKEN("Эта почта уже занята другим пользователем"),
    TOO_SHORT_PASSWORD("Пароль должен содержать минимум 6 символов"),
    TOO_SHORT_NEW_PASSWORD("Новый пароль должен содержать минимум 6 символов"),
    WRONG_PASSWORD("Неверный пароль"),
    SAME_PASSWORDS("Новый пароль равен старому"),
    WRONG_CREDENTIALS("Неверные логин и/или пароль");

    private final String description;

    Error(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
