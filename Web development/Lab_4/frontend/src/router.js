import Vue from 'vue'
import VueRouter from 'vue-router'
import Registration from "@/views/Registration";
import Main from "@/views/Main";
import NotFoundError from "@/views/Error";

Vue.use(VueRouter);

export default new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'default-page',
            component: Registration,
            beforeEnter: (to, from, next) => {
                (localStorage.getItem("jwt") !== null) ? next({name: 'app-page'}) : next({name: 'auth-page'});
            }
        },
        {
            path: '/auth',
            name: 'auth-page',
            component: Registration,

        },
        {
            path: '/app',
            name: 'app-page',
            component: Main,
            beforeEnter: (to, from, next) => {
                 if (localStorage.getItem("jwt")) next();
                 else next({
                     name: 'error-page-app',
                 });
            }
        },
        {
            path: '/*',
            name: 'error-page',
            component: NotFoundError,
            props: {
                default: true,
                errorCode: "404",
                errorMessage: "Данной страницы не сущуствует :("
            }
        },
        {
            path: '/error',
            name: 'error-page-app',
            component: NotFoundError,
            props: {
                default: true,
                errorCode: "401",
                errorMessage: "У вас нет доступа к приложению. Сначала авторизуйтесь!"
            }
        }
    ]
});