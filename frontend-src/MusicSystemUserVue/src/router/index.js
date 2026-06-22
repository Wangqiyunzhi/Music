/*
 * @Description:
 * @Author: Rabbiter
 * @Date: 2023-03-16 01:26:34
 */
import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/pages/Home'
import MyMusic from '@/pages/MyMusic'
import Singer from '@/pages/Singer'
import SongList from '@/pages/SongList'
import Notice from '@/pages/Notice'
import Search from '@/pages/Search'
import Lyric from '@/pages/Lyric'
import SignUp from '@/pages/SignUp'
import LoginIn from '@/pages/LoginIn'
import ForgotPassword from '@/pages/ForgotPassword'
import Setting from '@/pages/Setting'
import SingerAlbum from '@/pages/SingerAlbum'
import SongListAlbum from '@/pages/SongListAlbum'

Vue.use(Router)

/** 瑙ｅ喅閲嶅璺宠浆鎶ラ敊 */
const originalPush = Router.prototype.push
Router.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}
const originalReplace = Router.prototype.replace
Router.prototype.replace = function replace (location, onResolve, onReject) {
  if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
  return originalReplace.call(this, location).catch(err => err)
}

const router = new Router({
  mode: 'hash',
  routes: [
    { path: '/', name: 'home', component: Home },

    // 鉁?闇€瑕佺櫥褰曠殑椤甸潰锛氬姞 meta.requiresAuth
    { path: '/my-music', name: 'my-music', component: MyMusic, meta: { requiresAuth: true } },
    { path: '/setting', name: 'setting', component: Setting, meta: { requiresAuth: true } },

    { path: '/singer', name: 'singer', component: Singer },
    { path: '/song-list', name: 'song-list', component: SongList },
    { path: '/notice', name: 'notice', component: Notice },
    { path: '/search', name: 'search', component: Search },
    { path: '/lyric', name: 'lyric', component: Lyric },

    { path: '/sign-up', name: 'sign-up', component: SignUp },
    { path: '/login-in', name: 'login-in', component: LoginIn },
    { path: '/forgot-password', name: 'forgot-password', component: ForgotPassword },

    { path: '/singer-album/:id', name: 'singer-album', component: SingerAlbum },
    { path: '/song-list-album/:id', name: 'song-list-album', component: SongListAlbum },

    // 鍏滃簳
    { path: '*', redirect: '/' }
  ],
  scrollBehavior () {
    return { x: 0, y: 0 }
  }
})

/** 鐧诲綍鍒ゆ柇锛氬彧璁?userId锛堟渶绋筹級锛屾病鏈夊氨绠楁湭鐧诲綍 */
function isLogin () {
  return !!localStorage.getItem('userId')
}

/**
 * 鉁?瀹堝崼閫昏緫锛堢畝鍗曚笉鍧戯級锛?
 * - 浠呮嫤 requiresAuth 椤甸潰
 * - 涓嶆嫤 /login-in锛堝惁鍒欎綘浼氶亣鍒扳€滅偣鐧诲綍娌″弽搴斺€濓級
 */
router.beforeEach((to, from, next) => {
  const requiresAuth = !!to.meta.requiresAuth

  if (requiresAuth && !isLogin()) {
    return next({
      path: '/login-in',
      query: { redirect: to.fullPath }
    })
  }

  next()
})

export default router

