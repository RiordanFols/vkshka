
let login = new Vue({
    el: '#login',
    data: {
        error: frontendData.error,
        notification: frontendData.notification,
    },
    template:
        '<div class="middle">' +
            '<div class="middle-center">' +
                '<form method="post" action="/login">' +
                    '<h2 class="auth-header">Авторизация</h2>' +

                    '<p v-if="error !== null" class="auth-error-message">{{ error }}</p>' +
                    '<p v-if="notification !== null" class="auth-notification-message">{{ notification }}</p>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Имя пользователя</div>' +
                        '<input class="auth-input" type="text" maxlength="25" name="username" required autofocus>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Пароль</div>' +
                        '<input class="auth-input" type="password" maxlength="25" name="password" required>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<button class="auth-submit-btn" type="submit">Войти</button>' +
                    '</div>' +

                '</form>' +

                '<div class="auth-bottom-block">' +
                    '<p class="auth-bottom-caption">Нет аккаунта?' +
                        '<a class="auth-bottom-link" href="/registration">Регистрация</a>' +
                    '</p>' +
                '</div>' +
            '</div>' +
        '</div>'
});