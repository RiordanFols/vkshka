
Vue.component('sub-info', {
    props: ['sub', 'subs'],
    template:
        '<div class="sub-el">' +
            '<a v-bind:href="\'/user/\' + sub.username">' +
                '<img v-bind:src="\'/uploads/img/avatar/\' + sub.avatarFilename" class="sub-el-img" alt=""/>' +
            '</a>' +
            '<div class="sub-info">' +
                '<a v-bind:href="\'/user/\' + sub.username" style="text-decoration: none">' +
                    '<div class="sub-user-name">{{ sub.name }} {{ sub.surname }}</div>' +
                '</a>' +
            '</div>' +
        '</div>',
});

Vue.component('sub-list', {
    props: ['subs'],
    template:
        '<div class="sub-list">' +
            '<sub-info v-for="sub in subs" :key="sub.id" :subs="subs" :sub="sub"></sub-info>' +
        '</div>'
});

let subscribers = new Vue({
    el: '#subscribers',
    data: {
        subscribers: frontendData.subscribers,
    },
    template:
        '<div class="middle">' +
            '<sub-list :subs="subscribers"/>' +
        '</div>',
});