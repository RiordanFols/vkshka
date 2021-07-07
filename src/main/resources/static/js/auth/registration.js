
let registration = new Vue({
    el: '#registration',
    data: {
        genders: frontendData.genders,
        error: frontendData.error,
        notification: frontendData.notification,
        fd: frontendData.formData,

    },
    template:
        '<div class="middle">' +
            '<div class="middle-center">' +
                '<form method="post" action="/registration">' +
                    '<h2 class="auth-header">Регистрация</h2>' +

                    '<p v-if="error !== null" class="auth-error-message">{{ error }}</p>' +
                    '<p v-if="notification !== null" class="auth-notification-message">{{ notification }}</p>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Имя пользователя</div>' +
                        '<input class="auth-input" type="text" :value="fd.username" name="username" maxlength="25" required autofocus>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Выберите ваш пол</div>' +
                        '<select class="profile-input profile-gender-input" size="1" id="gender" name="gender" required>' +
                            '<option class="" v-for="gender in genders" v-bind:value="gender.name">{{ gender.description }}</option>' +
                        '</select>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Почта</div>' +
                        '<input class="auth-input" type="email" :value="fd.email" name="email" maxlength="129" required>' +
                    '</div>' +

                    '<br/>' +
                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Имя</div>' +
                        '<input class="auth-input" type="text" :value="fd.name" name="name" maxlength="25" required>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Фамилия</div>' +
                        '<input class="auth-input" type="text" :value="fd.surname" name="surname" maxlength="25">' +
                    '</div>' +

                    '<br/>' +
                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Пароль</div>' +
                        '<input class="auth-input" type="password" name="password" maxlength="25" required>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<div class="auth-label">Введите пароль еще раз</div>' +
                        '<input class="auth-input" type="password" name="passwordConfirm" maxlength="25" required>' +
                    '</div>' +

                    '<div class="auth-input-line">' +
                        '<button class="auth-submit-btn" type="submit">Готово</button>' +
                    '</div>' +

                '</form>' +

                '<div class="auth-bottom-block">' +
                    '<p class="auth-bottom-caption">Есть аккаунт?' +
                        '<a class="auth-bottom-link" href="/login">Авторизация</a>' +
                    '</p>' +
                '</div>' +
            '</div>' +
        '</div>'
});