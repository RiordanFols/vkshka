let postApi = Vue.resource('/post{/id}');
let postLikeApi = Vue.resource('/post-like{/id}');
let commentApi = Vue.resource('/comment{/id}');
let commentLikeApi = Vue.resource('/comment-like{/id}');
let replyApi = Vue.resource('/reply{/id}');
let replyLikeApi = Vue.resource('/reply-like{/id}');
let subscriptionApi = Vue.resource('/subscription{/id}');
let userInfoApi = Vue.resource('/user-info{/id}');

Vue.component('reply-el', {
    props: ['reply', 'replies', 'deleteReply', 'me'],
    data: function() {
        return {
            likeN: 0,
            isLiked: false,
            isFormVisible: false
        };
    },
    template:
        '<div class="reply-el">' +
            '<div class="reply-header">' +
                '<a v-bind:href="\'/user/\' + reply.author.username">' +
                    '<img class="reply-author-img" v-bind:src="\'/uploads/img/avatar/\' + reply.author.avatarFilename" alt=""/>' +
                '</a>' +
                '<div class="reply-info">' +
                    '<a v-bind:href="\'/user/\' + reply.author.username">' +
                        '<div class="reply-author">{{ reply.author.name }} {{ reply.author.surname }}</div>' +
                    '</a>' +
                    '<div class="reply-datetime">{{ reply.creationDateTime }}</div>' +
                '</div>' +

                '<div class="reply-action">' +
                    '<img class="reply-del-btn" src="/img/del_btn.png" @click="del" alt=""/>' +
                '</div>' +
            '</div>' +

            '<div class="reply-main">' +
                '<div class="reply-text">{{ reply.text }}</div>' +
                '<div class="reply-like-section">' +
                    '<img class="reply-btn" v-if="isLiked" @click="unlike" src="/img/liked.png" alt=""/>' +
                    '<img class="reply-btn" v-else="isLiked" @click="like" src="/img/unliked.png" alt=""/>' +
                    '<div class="reply-number">{{ likeN }}</div>' +
                    '<img class="reply-btn" src="/img/reply_btn.png" alt="" @click="switchForm"/>' +
                '</div>' +
            '</div>' +

            '<reply-form v-if="isFormVisible" :replies="replies" :comment="reply.comment" ' +
                        ':name="reply.author.name" :hideMethod="switchForm" :me="me"/>' +
        '</div>',
    methods: {
        like: function () {
            replyLikeApi.save({id: this.reply.id}, {}).then(result => {
                if (result.ok) {
                    this.isLiked = true;
                    this.likeN++;
                }
            });
        },
        unlike: function () {
            replyLikeApi.remove({id: this.reply.id}).then(result => {
                if (result.ok) {
                    this.isLiked = false;
                    this.likeN--;
                }
            });
        },
        del: function () {
            this.deleteReply(this.reply);
        },
        switchForm: function () {
            this.isFormVisible = !this.isFormVisible;
        }
    },
    created: function () {
        replyLikeApi.get({id: this.reply.id}).then(result => {
            result.json().then(data => {
                this.likeN = data.likeN;
                this.isLiked = data.isLiked;
            });
        });
    }
});

Vue.component('reply-section', {
    props: ['replies', 'me'],
    template:
        '<div class="reply-section">' +
            '<reply-el v-for="reply in replies" :reply="reply" :key="reply.id" ' +
                    ':deleteReply="deleteReply" :replies="replies" :me="me"/>' +
        '</div>',
    methods: {
        deleteReply: function (reply) {
            if (confirm("Вы уверены, что хотите удалить комментарий?")) {
                replyApi.remove({id: reply.id}).then(result => {
                    if (result.ok) {
                        this.replies.splice(this.replies.indexOf(reply), 1);
                    }
                });
            }
        }
    }
});

Vue.component('reply-form', {
    props: ['replies', 'comment', 'name', 'hideMethod', 'me'],
    data: function () {
        return {
            text: this.name + ', '
        };
    },
    template:
        '<div class="reply-form">' +
            '<img class="reply-form-img" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
            '<input class="reply-form-text" type="text" v-model="text"/>' +
            '<input class="reply-form-btn" type="button" value="✔" @click="save"/>' +
        '</div>',
    methods: {
        save: function () {
            if (this.text !== '' && this.text !== (this.name + ', ')) {
                let body = {text: this.text};
                replyApi.save({id: this.comment.id}, body).then(result => {
                    result.json().then(data => {
                        this.replies.push(data);
                        this.text = this.name + ', ';
                        this.hideMethod();
                    });
                });
            }
        }
    }
});

Vue.component('comment-form', {
    props: ['comments', 'post', 'me'],
    data: function () {
        return {
            text: ''
        };
    },
    template:
        '<div class="comment-form">' +
            '<img class="comment-form-img" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
            '<input class="comment-form-text" type="text" placeholder="Напишите комментарий" v-model="text"/>' +
            '<input class="comment-form-btn" type="button" value="✔" @click="save"/>' +
        '</div>',
    methods: {
        save: function () {
            let body = {text: this.text};
            commentApi.save({id: this.post.id}, body).then(result => {
                result.json().then(data => {
                    this.comments.push(data);
                    this.text = '';
                });
            });
        }
    }
});

Vue.component('comment-el', {
    props: ['comment', 'deleteComment', 'me'],
    data: function() {
        return {
            isLiked: false,
            likeN: 0,
            replies: [],
            isFormVisible: false
        };
    },
    template:
        '<div class="comment-el">' +
            '<div class="comment-header">' +
                '<a v-bind:href="\'/user/\' + comment.author.username">' +
                    '<img class="comment-author-img" v-bind:src="\'/uploads/img/avatar/\' + comment.author.avatarFilename" alt=""/>' +
                '</a>' +
                '<div class="comment-info">' +
                    '<a v-bind:href="\'/user/\' + comment.author.username">' +
                        '<div class="comment-author">{{ comment.author.name }} {{ comment.author.surname }}</div>' +
                    '</a>' +
                    '<div class="comment-datetime">{{ comment.creationDateTime }}</div>' +
                '</div>' +

                '<div class="comment-action">' +
                    '<img class="comment-del-btn" src="/img/del_btn.png" @click="del" alt=""/>' +
                '</div>' +
            '</div>' +

            '<div class="comment-main">' +
                '<div class="comment-text">{{ comment.text }}</div>' +
                '<div class="comment-like-section">' +
                    '<img class="comment-btn" v-if="isLiked" @click="unlike" src="/img/liked.png" alt=""/>' +
                    '<img class="comment-btn" v-else="isLiked" @click="like" src="/img/unliked.png" alt=""/>' +
                    '<div class="comment-number">{{ likeN }}</div>' +
                    '<img class="comment-btn" src="/img/reply_btn.png" alt="" @click="switchForm"/>' +
                '</div>' +
            '</div>' +

            '<reply-form v-if="isFormVisible" :replies="replies" :comment="comment" ' +
                        ':name="comment.author.name" :hideMethod="switchForm" :me="me"/>' +
            '<reply-section :replies="replies" :me="me"/>' +
        '</div>',
    methods: {
        like: function () {
            commentLikeApi.save({id: this.comment.id}, {}).then(result => {
                if (result.ok) {
                    this.isLiked = true;
                    this.likeN++;
                }
            });
        },
        unlike: function () {
            commentLikeApi.remove({id: this.comment.id}).then(result => {
                if (result.ok) {
                    this.isLiked = false;
                    this.likeN--;
                }
            });
        },
        del: function () {
            if (confirm("Вы уверены, что хотите удалить комментарий?"))
                this.deleteComment(this.comment);
        },
        switchForm: function () {
            this.isFormVisible = !this.isFormVisible;
        }
    },
    created: function () {
        commentLikeApi.get({id: this.comment.id}).then(result => {
            result.json().then(data => {
                this.likeN = data.likeN;
                this.isLiked = data.isLiked;
            });
        });

        replyApi.get({id: this.comment.id}).then(result => {
            result.json().then(data => {
                this.replies = data;
            });
        });
    }
});

Vue.component('comment-section', {
    props: ['comments', 'post', "me"],
    template:
        '<div class="comment-section">' +
            '<comment-form :comments="comments" :post="post" :me="me"/>' +
            '<comment-el v-for="comment in comments" :key="comment.id" ' +
                ':comment="comment" :deleteComment="deleteComment" :me="me"/>' +
        '</div>',
    methods: {
        deleteComment: function (comment) {
            commentApi.remove({id: comment.id}).then(result => {
                if (result.ok) {
                    this.comments.splice(this.comments.indexOf(comment), 1);
                }
            });
        }
    }
});

Vue.component('post-form', {
    props: ['me'],
    template:
        '<form action="/me" enctype="multipart/form-data" method="post">' +
            '<div class="post-form">' +
                '<img class="post-form-img" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
                '<input class="post-form-text" name="text" type="text" placeholder="Что у вас нового?"/>' +
                '<label for="images"><img class="post-form-files-label-img" src="/img/upload_btn.png" alt="/"></label>' +
                '<input class="post-form-files" id="images" name="images" type="file" multiple >' +
                '<input class="post-form-btn" type="submit" value="✔"/>' +
            '</div>' +
        '</form>',
});

Vue.component('post-img', {
    props: ['imgFilename'],
    template:
        '<img class="post-img" src="" v-bind:src="\'/uploads/img/content/\' + imgFilename" alt=""/>',
});

Vue.component('post-imgs', {
    props: ['post'],
    template:
        '<div class="post-image-section">' +
            '<post-img v-for="filename in post.imgFilenames" :key="filename" :imgFilename="filename"/>' +
        '</div>',
});

Vue.component('post-el', {
    props: ['post', 'deletePost', 'me'],
    data: function () {
        return {
            likeN: 0,
            isLiked: false,
            comments: [],
            commentsVisible: false,
        }
    },
    template:
        '<div class="post-el">' +
            '<div class="post-header">' +
                '<a v-bind:href="\'/user/\' + post.author.username">' +
                    '<img class="post-author-img" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
                '</a>' +

                '<div class="post-info">' +
                    '<a v-bind:href="\'/user/\' + post.author.username" style="text-decoration: none">' +
                        '<div class="post-author">{{ post.author.name }} {{ post.author.surname }}</div>' +
                    '</a>' +
                    '<div class="post-datetime">{{ post.creationDateTime }}</div>' +
                '</div>' +

                '<div class="post-action">' +
                    '<img class="post-del-btn" src="/img/del_btn.png" @click="del" alt=""/>' +
                    '<img class="post-edit-btn" src="/img/edit_btn.png" alt=""/>' +
                '</div>' +
            '</div>' +

            '<div class="post-main">' +
                '<div class="post-text">{{ post.text }}</div>' +
                '<post-imgs :post="post"/>' +
            '</div>' +

            '<div class="post-footer">' +
                '<img class="post-footer-btn" v-if="isLiked" @click="unlike" src="/img/liked.png" alt=""/>' +
                '<img class="post-footer-btn" v-else="isLiked" @click="like" src="/img/unliked.png" alt=""/>' +
                '<div class="post-footer-number">{{ this.likeN }}</div>' +

                '<img class="post-footer-btn" src="/img/comment_btn.png" alt="" @click="switchComments">' +
                '<div v-if="comments.length !== 0" class="post-footer-number">{{ comments.length }}</div>' +
            '</div>' +

            '<comment-section v-if="commentsVisible" :post="post" :comments="comments" :me="me"/>' +
        '</div>',
    methods: {
        del: function () {
            if (confirm("Вы уверены, что хотите удалить пост?"))
                this.deletePost(this.post);
        },
        like: function () {
            postLikeApi.save({id: this.post.id}, {}).then(result => {
                if (result.ok) {
                    this.isLiked = true;
                    this.likeN++;
                }
            });
        },
        unlike: function () {
            postLikeApi.remove({id: this.post.id}).then(result => {
                if (result.ok) {
                    this.isLiked = false;
                    this.likeN--;
                }
            });
        },
        switchComments: function () {
            this.commentsVisible = !this.commentsVisible;
        }
    },
    created: function () {
        postLikeApi.get({id: this.post.id}).then(result => {
            result.json().then(data => {
                this.likeN = data.likeN;
                this.isLiked = data.isLiked;
            });
        });

        commentApi.get({id: this.post.id}).then(result => {
            result.json().then(data => {
                this.comments = data;
                this.commentsVisible = this.comments.length !== 0;
            });
        });
    },
});

Vue.component('post-list', {
    props: ['posts', 'me'],
    template:
        '<div class="post-list">' +
            '<post-el v-for="post in posts" :key="post.id" :post="post" ' +
                ':posts="posts" :deletePost="deletePost" :me="me"/>' +
        '</div>',
    methods: {
        deletePost: function (post) {
            postApi.remove({id: post.id}).then(result => {
                if (result.ok)
                    this.posts.splice(this.posts.indexOf(post), 1);
            });
        }
    }
});

Vue.component('me-info', {
    props: ['me'],
    data: function () {
        return {
            subscribersN: 0,
            subscriptionsN: 0,
            birthday: null,
            age: null
        }
    },
    template:
        '<div class="user-info">' +
            '<div class="user-info-left">' +
                '<img class="user-photo" v-bind:src="\'/uploads/img/avatar/\' + me.avatarFilename" alt=""/>' +
            '</div>' +
            '<div class="user-info-right">' +
                '<div class="user-info-right-top">' +
                    '<div class="user-name">{{ me.name }} {{ me.surname }}</div>' +
                    '<div class="user-activity"></div>' +
                '</div>' +
                '<div class="user-info-right-other">' +
                    '<div class="user-status">{{ me.status }}</div>' +
                    '<div class="user-info-line">Пол: {{ me.gender }}</div>' +
                    '<div class="user-info-line">День рождения: {{ birthday }}</div>' +
                    '<div class="user-info-line">Возраст: {{ age }}</div>' +
                '</div>' +
                '<div class="user-info-right-footer">' +
                    '<a href="/subscribers"><div class="user-info-footer-box">' +
                        '<div class="user-info-footer-box-caption">Подписчики</div>' +
                        '<div class="user-info-footer-box-number">{{ subscribersN }}</div>' +
                    '</div></a>' +
                    '<a href="/subscriptions"><div class="user-info-footer-box">' +
                        '<div class="user-info-footer-box-caption">Подписки</div>' +
                        '<div class="user-info-footer-box-number">{{ subscriptionsN }}</div>' +
                    '</div></a>' +
                '</div>' +
            '</div>' +
        '</div>',
    created: function () {
        subscriptionApi.get({id: this.me.id}).then(result => {
            result.json().then(data => {
                this.subscribersN = data.subscribersN;
                this.subscriptionsN = data.subscriptionsN;
            });
        });

        userInfoApi.get({id: this.me.id}).then(result => {
            result.json().then(data => {
                this.birthday = data.birthday;
                this.age = data.age;
            });
        });
    }
});

let me = new Vue({
    el: '#me',
    data: {
        me: frontendData.me,
        posts: frontendData.userPosts,
    },
    template:
        '<div class="middle">' +
            '<me-info :me="me"/>' +
            '<post-form :me="me"/>' +
            '<post-list :posts="posts" :me="me"/>' +
        '</div>',
});