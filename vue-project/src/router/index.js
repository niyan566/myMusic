import {createRouter,createWebHistory} from 'vue-router'

const router=createRouter({
    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {path:'/',redirect:'/home/musicLibrary'},
        {path:'/home',name:'home',component:()=>import('../views/Home.vue'),children:[
            {path:'musicLibrary',name:'musicLibrary',component:()=>import('@/views/MusicLibrary.vue')},
            {path:'likedMusic',name:'likedMusic',component:()=>import('@/views/LikedMusic.vue')},
            {path:'recently',name:'recently',component:()=>import('@/views/Recently.vue')},
            {path:'playLists',name:'playLists',component:()=>import('@/views/PlayList.vue')},
            {path:'playList/:id',name:'playList',component:()=>import('@/views/PlayListDetail.vue')},
        ]},
        {path:'/upload',name:'upload',component:()=>import('../views/Upload.vue')}
    ]
})


export default router