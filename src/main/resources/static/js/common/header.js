
let header = new Vue({
    el: '#header',
    data: {
        me: frontendData.me,
    },
    template:
        '<div class="header">' +
            '<div class="header-content">' +
                '<div class="header-app-name">Простоквашино</div>' +
                '<div class="header-middle">' +
                    '<div></div>' +
                    '<div></div>' +
                    '<div></div>' +
                '</div>' +
                '<a href="/me">' +
                    '<div v-if="me != null" class="header-profile">' +
                        '<img class="header-profile-img" src="" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
                        '<div class="header-profile-name">{{ me.name }} {{ me.surname }}</div>' +
                    '</div>' +
                '</a>' +
                '<a v-if="me != null" class="header-logout" href="/logout">Выйти</a>' +
            '</div>' +
        '</div>'
});