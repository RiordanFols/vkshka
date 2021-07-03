let profilePhotoApi = Vue.resource('/profile/photo');

Vue.component('user-profile', {
    props: ['me', 'updateAvatarMethod', 'genders', 'error', 'notification', 'passwordError', 'passwordNotification'],
    template:
        '<div class="user-info">' +
            '<div class="user-info-left">' +
                '<img class="user-photo" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
                '<form action="/profile/update/avatar" method="post" enctype="multipart/form-data">' +
                    '<label for="images" class="avatar-label"><img class="avatar-label-img" src="/img/upload_btn.png" alt="/"></label>' +
                    '<input class="profile-img-update" id="images" name="avatar" type="file" >' +
                    '<input class="profile-img-submit" type="submit" value="Обновить фото"/>' +
                '</form>' +
                '<div class="profile-img-delete" @click="deletePhoto">Удалить фото</div>' +
            '</div>' +
            '<div class="user-info-right">' +

                '<p v-if="error !== null" class="profile-error-message">{{ error }}</p>' +
                '<p v-if="notification !== null" class="profile-notification-message">{{ notification }}</p>' +

                '<form action="profile/update/data" method="post">' +

                    '<div class="user-info-right-names">' +

                        '<div class="profile-input-line">' +
                            '<div class="profile-label user-name-label">Имя: </div>' +
                            '<input class="profile-input user-name-input" name="name" required maxlength="25" type="text" v-model="me.name"/><br/>' +
                        '</div>' +

                        '<div class="profile-input-line">' +
                            '<div class="profile-label user-name-label">Фамилия: </div>' +
                            '<input class="profile-input user-name-input" name="surname" required maxlength="25" type="text" v-model="me.surname"/><br/>' +
                        '</div>' +

                        '<div class="profile-input-line">' +
                            '<div class="profile-label">Юзернейм: </div>' +
                            '<input class="profile-input" name="username" type="text" required maxlength="25" v-model="me.username"/>' +
                        '</div>' +

                    '</div>' +
                    '<div class="user-info-right-other">' +
                        '<div class="profile-input-line">' +
                            '<div class="profile-label">Почта: </div>' +
                            '<input class="profile-input" name="email" type="email" required maxlength="129" v-model="me.email"/>' +
                        '</div>' +

                        '<div class="profile-input-line">' +
                            '<div class="profile-label" >Пол: </div>' +
                            '<select class="profile-input profile-gender-input" size="1" id="gender" name="gender" required>' +
                                '<option v-for="gender in genders" v-if="me.gender == gender.description" ' +
                                        'v-bind:value="gender.name" selected>{{ gender.description }}</option>' +
                                '<option v-else v-bind:value="gender.name">{{ gender.description }}</option>' +
                            '</select>' +
                        '</div>' +

                        '<div class="profile-input-line">' +
                            '<div class="profile-label" >Статус: </div>' +
                            '<input class="profile-input" name="status" maxlength="50" type="text" v-model="me.status"/>' +
                        '</div>' +

                        '<div class="profile-input-line profile-birthday-input-line">' +
                            '<div class="profile-label">День рождения: </div>' +
                            '<input class="profile-input user-birthday-input" name="birthday" type="date" v-model="me.birthday"/>' +
                        '</div>' +

                        '<input class="profile-submit" type="submit" value="Готово"/>' +
                    '</div>' +
                '</form>' +

                '<p v-if="passwordError !== null" class="profile-error-message">{{ passwordError }}</p>' +
                '<p v-if="passwordNotification !== null" class="profile-notification-message">{{ passwordNotification }}</p>' +

                '<form action="profile/update/password" method="post">' +
                    '<div class="user-info-right-password">' +
                        '<div class="profile-input-line">' +
                            '<div class="profile-label-password">Старый пароль</div>' +
                            '<input class="profile-input-password" name="oldPassword" type="password">' +
                        '</div>' +
                        '<div class="profile-input-line">' +
                            '<div class="profile-label-password">Новый пароль</div>' +
                            '<input class="profile-input-password" name="newPassword" type="password">' +
                        '</div>' +
                        '<div class="profile-input-line">' +
                            '<div class="profile-label-password">Подтвердите новый пароль</div>' +
                            '<input class="profile-input-password" name="newPasswordConfirm" type="password">' +
                        '</div>' +

                        '<input class="profile-submit" type="submit" value="Готово"/>' +
                    '</div>' +
                '</form>' +
            '</div>' +
        '</div>',
    methods: {
        deletePhoto: function () {
            if (confirm("Вы уверены, что хотите удалить своё фото?")) {
                profilePhotoApi.remove().then(result => {
                    result.json().then(data => {
                        this.updateAvatarMethod(data);
                    });
                });
            }
        }
    }
});

let profile = new Vue({
    el: '#profile',
    data: {
        me: frontendData.me,
        genders: frontendData.genders,
        error: frontendData.error,
        notification: frontendData.notification,
        passwordError: frontendData.passwordError,
        passwordNotification: frontendData.passwordNotification,
    },
    template:
        '<div class="middle">' +
            '<user-profile :me="me" :updateAvatarMethod="updateUser" :genders="genders"' +
                ' :error="error" :notification="notification"' +
                ' :passwordError="passwordError" :passwordNotification="passwordNotification"/>' +
        '</div>',
    methods: {
        updateUser: function (user) {
            this.me = user;
        }
    },
});