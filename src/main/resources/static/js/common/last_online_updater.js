let sessionApi = Vue.resource('/session');

let updater = new Vue({
    el: '#lastOnlineUpdater',
    created: function () {
        sessionApi.update();
        setInterval(function () {
            sessionApi.update();
        }, 60000);
    }
});