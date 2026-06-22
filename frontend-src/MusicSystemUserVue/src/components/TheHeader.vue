<template>
  <div class="the-header">
    <div class="header-back" v-if="showBack" @click="goBack" title="返回上一页">
        <i class="el-icon-arrow-left"></i>
        <span>返回</span>
    </div>
    <div class="header-logo" @click="goHome">
        <img :src="require('../assets/img/logo02.jpg')" style="height: 32px;">
        <span class="logo-text">音乐馆</span>
    </div>
    <ul class="navbar">
        <li :class="{active: item.name == activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path,item.name)">
            {{item.name}}
        </li>
        <li class="nav-search">
            <div class="header-search">
                <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords" style="width:180px">
                <div class="search-btn" @click="goSearch()">
                    <i class="iconfont icon-r-find" style="font-size: 28px;color:white;"></i>
                </div>
            </div>
        </li>
        <li v-show="!loginIn" class="nav-ghost" :class="{active: item.name == activeName}" v-for="item in loginMsg" :key="item.path" @click="goPage(item.path,item.name)">
            {{item.name}}
        </li>
    </ul>
    <div class="header-right" v-show="loginIn">
        <div id='user'>
            <img :src='attachImageUrl(avator)'>
        </div>
        <ul class="menu">
            <li v-for="(item,index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
        </ul>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import {navMsg,loginMsg,menuList} from '../assets/data/header';
import { userLogout } from '../api/index'

export default {
  name: 'the-header',
  data() {
      return {
          navMsg: [],
          keywords: '',
          loginMsg: [],
          menuList: [],
      }
  },
  computed:{
      ...mapGetters([
          'activeName',
          'loginIn',
          'avator'
      ]),
      showBack() {
          const path = (this.$route && this.$route.path) || '';
          if (path === '/' || path === '/login-in' || path === '/sign-up') return false;
          if (path === '/song-list') return false;
          if (path === '/singer') return false;
          if (path === '/notice') return false;
          if (path === '/my-music') return false;
          return true;
      }
  },
  created() {
      this.navMsg = navMsg;
      this.loginMsg = loginMsg;
      this.menuList = menuList;
  },
  mounted(){
      document.querySelector('#user').addEventListener('click',function(e){
          document.querySelector('.menu').classList.add("show");
          e.stopPropagation()
      },false);
      document.querySelector('.menu').addEventListener('click',function(e){
          e.stopPropagation()
      },false);
      document.addEventListener('click',function(){
        document.querySelector('.menu').classList.remove('show');
      },false);
  },

  methods: {
        notify(title,type) {
            this.$notify({
                title: title,
                type: type
            })
        },
      goHome() {
          this.$router.push({path: "/"});
      },
      goBack() {
          if (window.history.length > 1) {
              this.$router.back();
          } else {
              this.$router.push({ path: "/" });
          }
      },
      goPage(path,name) {
          if(!this.loginIn && path=='/my-music'){
              this.notify('请先登录','warning');
          }else{
            this.$store.commit('setActiveName',name);
            this.$router.push({path: path});
          }
      },
      goSearch(){
          this.$router.push({path:'/search',query:{keywords: this.keywords}})
      },
      attachImageUrl (srcUrl) {
        return srcUrl? this.$store.state.configure.HOST+srcUrl : '../assets/img/user.jpg';
      },
   async goMenuList(path) {
  if (path === 0) {
    try {
      await userLogout()
    } catch (e) {}

    this.$store.commit('setLoginIn', false)
    this.$store.commit('setIsActive', false)
    this.$store.commit('setUserId', '')
    this.$store.commit('setUsername', '')
    this.$store.commit('setAvator', '')

    localStorage.removeItem('userId')
    localStorage.removeItem('userName')
    localStorage.removeItem('avator')

    sessionStorage.removeItem('userId')
    sessionStorage.removeItem('username')
    sessionStorage.removeItem('avator')
    sessionStorage.removeItem('loginIn')
    sessionStorage.removeItem('isActive')
    sessionStorage.removeItem('activeName')
    sessionStorage.removeItem('showAside')

    this.$router.replace('/login-in')

    const menu = document.querySelector('.menu')
    if (menu) menu.classList.remove('show')

    return
  }

  this.$router.push({ path })
}


  }

}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-header.scss';
</style>
