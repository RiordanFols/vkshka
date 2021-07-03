
let leftMenu = new Vue({
    el: '#leftMenu',
    data: {
        // часть url после :PORT и до параметров
        curUrl: currentUrl.split(":")[2].substring(4).split("?")[0],
        activeBtn: 0,
    },
    template:
        '<div class="left">' +
            '<a href="/profile">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===1">Личный кабинет</div>' +
                '<div class="left-btn" v-else>Личный кабинет</div>' +
            '</a>' +

            '<a href="/me">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===2">Моя страница</div>' +
                '<div class="left-btn" v-else>Моя страница</div>' +
            '</a>' +

            '<a href="/feed">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===3">Новости</div>' +
                '<div class="left-btn" v-else>Новости</div>' +
            '</a>' +

            '<a href="/messenger">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===4">Сообщения</div>' +
                '<div class="left-btn" v-else>Сообщения</div>' +
            '</a>' +

            '<a href="/subscriptions">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===5">Подписки</div>' +
                '<div class="left-btn" v-else>Подписки</div>' +
            '</a>' +

            '<a href="/subscribers">' +
                '<div class="left-btn active-left-btn" v-if="activeBtn===6">Подписчики</div>' +
                '<div class="left-btn" v-else>Подписчики</div>' +
            '</a>' +

        '</div>',
    created: function () {
        switch (this.curUrl) {
            case "/profile":
                this.activeBtn = 1;
                break;
            case "/me":
                this.activeBtn = 2;
                break;
            case "/feed":
                this.activeBtn = 3;
                break;
            case "/messenger":
                this.activeBtn = 4;
                break;
            case "/subscriptions":
                this.activeBtn = 5;
                break;
            case "/subscribers":
                this.activeBtn = 6;
                break;
        }
    }
});